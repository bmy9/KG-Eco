import bs4 as bs
import requests
import pickle
def GetHuStock():
    res = requests.get('https://www.banban.cn/gupiao/list_sh.html')
    res.encoding = res.apparent_encoding
    soup = bs.BeautifulSoup(res.text,'lxml')
    content = soup.find('div',{'class':'u-postcontent cz'})
    result= []
    for item in content.findAll('a'):
        result.append(item.text)
    with open('huStock.pickle','wb') as f:
        pickle.dump(result, f)
if __name__ == '__main__':
    GetHuStock()
    import datetime as dt
    import pandas as pd
    import pandas_datareader.data as web
    from matplotlib import style
    import matplotlib.pyplot as plt
    import os
    import pickle
    def GetStockFromYahoo(isHaveStockCode=False):
        if not isHaveStockCode:
            GetHuStock()
        with open('huStock.pickle', 'rb') as f:
            tickets = pickle.load(f, encoding='gb2312')
        if not os.path.exists('StockDir'):
            os.makedirs('StockDir')

        for ticket in tickets:
            arr = ticket.split('(')
            stock_name = arr[0]
            stock_code = arr[1][:-1] + '.ss'
            if os.path.exists('StockDir/{}.csv'.format(stock_name + stock_code)):
                print('已下载')
            else:
                print('下载{}中...'.format(stock_name))
                DownloadStock(stock_name, stock_code)
                print('下载{}中...'.format(stock_name))


    def DownloadStock(stockName, stockCode):
        style.use('ggplot')
        start = dt.datetime(2010, 1, 1)
        end = dt.datetime(2019, 8, 20)
        df = web.DataReader(stockCode, 'yahoo', start, end)
        df.to_csv('StockDir/{}.csv'.format(stockName + stockCode))
    GetStockFromYahoo(True)