import socket
import json
import numpy as np
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
from tensorflow.keras.models import load_model

# json 파일 가져오기
with open("data.json", "r", encoding="utf-8") as json_file:
    json_data = json.load(json_file)

# 인공지능 모델 불러오기
model = load_model('./model/model.h5')

# 안드로이드 연동
host = '192.168.98.16'  # 호스트 ip
port = 8080            # 포트번호

server_sock = socket.socket(socket.AF_INET) 
server_sock.bind((host, port)) 
server_sock.listen(1) 
print("기다리는 중")

client_sock, addr = server_sock.accept()
if client_sock:
    print('Connected by', addr) 
    in_data = client_sock.recv(1024)
    print(in_data.decode("utf-8"), len(in_data)) 

    command = in_data.decode("utf-8")[2:].split('-')
    id_name = command[0]
    salary = int(command[1])
    mode = command[2]
    if mode == '2':
        to_use_expense = int(command[3]) * 10000

    print(command)

    # 이름에 맞는 데이터 가져오기
    for j in json_data['data_list']:
        if j['id'] == id_name:
            selected_data = j

    # json 읽어와 데이터프레임으로 저장
    data = []
    for j in selected_data['transaction']:
        sample = {'year':j['year'], 'month':j['month']}
        for a in j['amount']:
            sample[f"{a['type']}_total"] = a['total']
            sample[f"{a['type']}_normal"] = a['normal']
            sample[f"{a['type']}_public_transport"] = a['public_transport']
            sample[f"{a['type']}_cultural_cost"] = a['cultural_cost']
            sample[f"{a['type']}_traditional_market"] = a['traditional_market']
        data.append(sample)
    data = pd.DataFrame(data)
    data['total'] = data['신용카드_total'] + data['직불카드_total'] + data['현금영수증_total']

    # 지출금액 예측을 위한 전처리
    time_step = 12

    scaler = MinMaxScaler()
    scaler.fit(data[['total']])
    x = scaler.transform(data.iloc[-time_step:,-1:].values)
    x = x.reshape(1, time_step, 1)

    # 예측결과
    y_pred = scaler.inverse_transform(model.predict(x))
    y_pred = y_pred.astype('int')

    # 계산
    salary *= 10000

    if salary >= 15000000:
        minimum_deductible_amount = salary * 0.25
    else:
        minimum_deductible_amount = salary * 0.20

    if salary <= 70000000:
        deduction_limit = 3000000
    elif salary <= 120000000:
        deduction_limit = 2500000
    else:
        deduction_limit = 2000000

    remain_month = data.iloc[-1, 1]
    year_total_data = data.iloc[-remain_month:,:].sum()

    used_credit_card_expense = year_total_data['신용카드_normal']
    used_debit_card_expense = year_total_data['직불카드_normal'] + year_total_data['현금영수증_normal']

    used_public_transport_expense = year_total_data['신용카드_public_transport'] + year_total_data['직불카드_public_transport'] + year_total_data['현금영수증_public_transport']
    if used_public_transport_expense > 2500000:
        if year_total_data['신용카드_public_transport'] - 2500000 > 0:
            used_credit_card_expense += year_total_data['신용카드_public_transport'] - 2500000
            used_debit_card_expense += year_total_data['직불카드_public_transport'] + year_total_data['현금영수증_public_transport']
        else:
            used_debit_card_expense += used_public_transport_expense - 2500000

    if salary > 70000000:
        used_credit_card_expense += year_total_data['신용카드_cultural_cost']
        used_debit_card_expense += year_total_data['직불카드_cultural_cost'] + year_total_data['현금영수증_cultural_cost']
    else:
        used_cultural_cost_expense = year_total_data['신용카드_cultural_cost'] + year_total_data['직불카드_cultural_cost'] + year_total_data['현금영수증_cultural_cost']
        if used_cultural_cost_expense > 3333333:
            if year_total_data['신용카드_cultural_cost'] - 3333333 > 0:
                used_credit_card_expense += year_total_data['신용카드_cultural_cost'] - 3333333
                used_debit_card_expense += year_total_data['직불카드_cultural_cost'] + year_total_data['현금영수증_cultural_cost']
            else:
                used_debit_card_expense += used_cultural_cost_expense - 3333333

    used_traditional_market_expense = year_total_data['신용카드_traditional_market'] + year_total_data['직불카드_traditional_market'] + year_total_data['현금영수증_traditional_market']
    if used_traditional_market_expense > 2500000:
        if year_total_data['신용카드_traditional_market'] - 2500000 > 0:
            used_credit_card_expense += year_total_data['신용카드_traditional_market'] - 2500000
            used_debit_card_expense += year_total_data['직불카드_traditional_market'] + year_total_data['현금영수증_traditional_market']
        else:
            used_debit_card_expense += used_traditional_market_expense - 2500000

    total_used_expense = used_credit_card_expense + used_debit_card_expense
    print(mode == '1')
    if mode == '1':
        total_to_use_expense = y_pred[0, :12-remain_month].sum()
    elif mode == '2':
        total_to_use_expense = to_use_expense

    total_expense = total_used_expense + total_to_use_expense

    if total_expense <= minimum_deductible_amount:
        to_use_credit_card_expense = total_to_use_expense
        to_use_debit_card_expense = 0
        income_deduction = 0
        income_deduction_credit_card = 0
    elif total_expense - minimum_deductible_amount <= deduction_limit * 10 // 3:
        if used_credit_card_expense > minimum_deductible_amount:
            to_use_credit_card_expense = 0
            to_use_debit_card_expense = total_to_use_expense
            income_deduction = (used_credit_card_expense + to_use_credit_card_expense - minimum_deductible_amount) * 0.15 + (used_debit_card_expense + to_use_debit_card_expense) * 0.30
        elif used_debit_card_expense > total_expense - minimum_deductible_amount:
            to_use_credit_card_expense = total_to_use_expense
            to_use_debit_card_expense = 0
            if used_credit_card_expense + to_use_credit_card_expense - minimum_deductible_amount > 0:
                income_deduction = (used_credit_card_expense + to_use_credit_card_expense - minimum_deductible_amount) * 0.15 + (used_debit_card_expense + to_use_debit_card_expense) * 0.30
            else:
                income_deduction = (total_expense - minimum_deductible_amount) * 0.30
        else:
            to_use_credit_card_expense = (minimum_deductible_amount - used_credit_card_expense)
            to_use_debit_card_expense = (total_expense - minimum_deductible_amount - used_debit_card_expense)
            income_deduction = (total_expense - minimum_deductible_amount) * 0.30
        income_deduction_credit_card = (total_expense - minimum_deductible_amount) * 0.15
    else:
        excess_amount = total_expense - minimum_deductible_amount - deduction_limit * 10 // 3
        if deduction_limit * 10 // 3 < excess_amount + used_debit_card_expense:
            to_use_credit_card_expense = total_to_use_expense
            to_use_debit_card_expense = 0
        else:
            to_use_credit_card_expense = (minimum_deductible_amount + 2 * excess_amount - used_credit_card_expense)
            to_use_debit_card_expense = (deduction_limit * 10 // 3 - excess_amount - used_debit_card_expense)
        income_deduction = deduction_limit
        if (used_credit_card_expense + total_to_use_expense) > minimum_deductible_amount:
            income_deduction_credit_card = min((used_credit_card_expense + total_to_use_expense - minimum_deductible_amount) * 0.15 + used_debit_card_expense * 0.30, deduction_limit)
        else:
            income_deduction_credit_card = min((total_expense - minimum_deductible_amount) * 0.30, deduction_limit)

    if salary <= 5000000:
        earned_income_amount = salary - salary * 0.70
    elif salary <= 15000000:
        earned_income_amount = salary - (3500000 + (salary - 5000000) * 0.40)
    elif salary <= 45000000:
        earned_income_amount = salary - (7500000 + (salary - 15000000) * 0.15)
    elif salary <= 100000000:
        earned_income_amount = salary - (12000000 + (salary - 45000000) * 0.05)
    else:
        earned_income_amount = salary - (14750000 + (salary - 100000000) * 0.02)

    tax_base = earned_income_amount - income_deduction
    if tax_base <= 12000000:
        tariff = 0.06
    elif tax_base <= 46000000:
        tariff = 0.15
    elif tax_base <= 88000000:
        tariff = 0.24
    elif tax_base <= 150000000:
        tariff = 0.35
    elif tax_base <= 300000000:
        tariff = 0.38
    elif tax_base <= 500000000:
        tariff = 0.40
    else:
        tariff = 0.42


    print("총급여 : ", salary)
    print("근로소득금액 : ", earned_income_amount)
    print("카드소득공제 최저사용금액 : ", minimum_deductible_amount)
    print("적용과세표준 : ", tariff)
    print("나의 연간 카드사용 예측금액 : ", total_expense)
    print("연말까지 사용할 예측금액 : ", total_to_use_expense)
    print("황금비율 신용카드(연말까지 사용할 금액) : ", round(to_use_credit_card_expense))
    print("황금비율 체크카드(연말까지 사용할 금액) : ", round(to_use_debit_card_expense))
    print("황금비율 신용카드(월) : ", round(to_use_credit_card_expense) // remain_month)
    print("황금비율 체크카드(월) : ", round(to_use_debit_card_expense) // remain_month)
    print("실제 공제대상 : ", round(income_deduction))
    print("실제 세제혜택 : ", round(income_deduction * tariff))
    print("카드사용금액을 전부 신용카드로만 썼을 때 세제혜택 : ", round(income_deduction_credit_card * tariff))

    
    out_data = "-".join(list(map(str, [id_name,
                         selected_data['name'],
                         mode,
                         salary,
                         total_expense, 
                         round(income_deduction_credit_card * tariff),
                         total_to_use_expense,
                         round(to_use_credit_card_expense) // remain_month,
                         round(to_use_debit_card_expense) // remain_month,
                         round(to_use_credit_card_expense),
                         round(to_use_debit_card_expense),
                         round(income_deduction * tariff),
                         round(income_deduction * tariff)-round(income_deduction_credit_card * tariff)])))
    print(out_data)
    print(str(out_data))
    client_sock.send(str(out_data).encode('utf-8'))

client_sock.close()
server_sock.close()