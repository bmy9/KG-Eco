import pdfplumber
with pdfplumber.open(r'C:\Users\xgfdehuawei\Desktop/1.pdf') as pdf:
    page = pdf.pages[162]
    for row in page.extract_table():
        print(row)
        print(row[0])