import sys
import os

sys.path.insert(0, os.path.dirname(os.getcwd()))

import unittest
from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
import csv
import time
from ChromeSeleniumFunctions import *

class ProductFormTest(unittest.TestCase):
    def setUp(self):
        # Configura as opções do Chrome.
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")

        # Iniciliza o WebDriver.
        self.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
        self.driver.get("http://localhost:8080/showAllClients")

    def test_create_tons_of_clients(self):
        '''
        # -- FUNÇÕES CRIADAS PARA TESTES --
        # create_client(self, nome, email, senha, nascimento, cpf,
        #               ativo=1, M=1;F=2;?=3)
        # create_phone(self, fixo=1, ddd, numero)
        # create_address(self, entrega=1, cobrança=1, apelido, casa, rua, logradouro,
        #                numero, cep, bairro, cidade, estado, observação="")
        '''
        with open('clients.csv', mode='r', encoding='utf-8') as csvfile:
            reader = csv.DictReader(csvfile)

            for row in reader:
                create_client(self, row['Nome'], row['Email'], row['Senha'], row['Nascimento'], row['CPF'], row['Ativo'], row['Gênero'],)
                create_phone(self, row['Fixo'], row['DDD'], row['Número de Telefone'])
                create_address(self, row['Entrega'], row['Cobrança'], row['Apelido'], row['Casa'], row['Rua'], row['Logradouro'], row['Número da Casa'], row['CEP'], row['Bairro'], row['Cidade'], row['Estado'], row['Observação'])


    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
