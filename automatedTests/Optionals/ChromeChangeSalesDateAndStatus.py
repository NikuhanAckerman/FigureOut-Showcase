import sys
import os

sys.path.insert(0, os.path.dirname(os.getcwd()))

import unittest
from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
import time
import csv
from ChromeSeleniumFunctions import *

class ProductFormTest(unittest.TestCase):
    def setUp(self):
        # Configura as opções do Chrome
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")

        # Iniciliza o WebDriver
        self.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
        self.driver.get("http://localhost:8080/sales/seeSales")

    def test_fill_credit_cards_form(self):
        '''
        # -- FUNÇÕES CRIADAS PARA TESTES --
        # click_button(self, id): Clica em botão da página.
        # input_string(self, id, valor): Insere texto.
        # select_radio(self, id, valor): Seleciona botão de rádio.
        # check_checkbox(self, id): Checha checkbox.
        # select_option(self, id, valor): Seleciona opção de um menu dropdown.
        # select_option_by_value(self, id, value): Seleciona opção de menu dropdown pelo valor.
        # send_image(self, id, pasta, arquivo): Envia uma imagem.
        '''
        
        with open('dates.csv', mode='r', encoding='utf-8') as csvfile:
            reader = csv.DictReader(csvfile)

             # Iterando sobre as linhas do CSV
            for i, row in enumerate(reader, 1):  # i começa de 1
                if i > 40:  # Limita a leitura a 40 linhas
                    break

                ## IDs dinâmicos
                change_date = "inputDate" + str(i)
                change_date_button = "changeDate" + str(i)
                sale_status = "saleStatus" + str(i)
                
                print()
                print("======== ITERAÇÃO "+ str(i) + " ========")
                
                # Insere data na caixa de inserção.
                input_string(self, change_date, row['Datas'])
                
                # Clica no botão "Mudar" para mudar a data.
                click_button(self, change_date_button)

                # Clica no botão "Mudar" para mudar a data.
                # select_option_by_value(self, sale_status, "ENTREGUE")

                # Imprime no console
                print("Data e status da venda de ID " + str(i) + " trocada!")

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
