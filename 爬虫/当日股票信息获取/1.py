import requests
import json
import pandas as pd
import numpy as np
import re
url = r'http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?&type=CT&token=4f1862fc3b5e77c150a2b985b12db0fd&sty=FCOIATC&cmd=C._A&st=(ChangePercent)&sr=-1&p=1&ps=3700'

html = requests.get(url)
html = html.text

stock = re.findall(re.compile('\((.*)\)'), html)
stock = stock[0]
stock = stock.split('","')

stock[0] = stock[0][2:]
stock[-1] = stock[-1][:-2]
stock_data = []
for i, item in enumerate(stock):
    t = item.split(',')
    stock_data.append(t)

print(stock_data)
columns = ['交易所','股票代码','股票名字','当前股价','涨跌额','涨跌幅','成交量（手）','成交额','振幅','最高','最低','今开','昨收','5分钟涨跌','量比','换手率','市盈率','市净率','总市值','流通市值','60日涨跌幅','年初至今涨跌幅','涨速','上市时间','数据时间','成交量']
data = pd.DataFrame(stock_data,columns = columns)
#data.head().T

columns = ['交易所','当前股价', '涨跌额', '涨跌幅', '成交量（手）', '成交额', '振幅',
       '最高', '最低', '今开', '昨收', '5分钟涨跌', '量比', '换手率',  '市净率', '总市值', '流通市值',
       '成交量']
#data[columns] = data[columns].astype(np.float64)

data['市盈率'] = data['市盈率'].apply(lambda x:None if x == '-' else x)
data['市盈率'] = data['市盈率'].astype(np.float64)

data['上市时间'] = pd.to_datetime(data['上市时间'],format= '%Y-%m-%d')
data['数据时间'] = pd.to_datetime(data['数据时间'],format= '%Y-%m-%d %H:%M:%S')

data['60日涨跌幅'] = data['60日涨跌幅'].str.strip("%").astype(float)/100
data['年初至今涨跌幅'] = data['年初至今涨跌幅'].str.strip("%").astype(float)/100

data['涨速'] = data['涨速'].apply(lambda x:None if x == '-' else x)
data['涨速'] = data['涨速'].astype(np.float64)

data['交易所_'] =data['交易所'].apply(lambda x:'沪市' if x == 1 else '深市' )
data.to_csv('stock-bc.csv')
