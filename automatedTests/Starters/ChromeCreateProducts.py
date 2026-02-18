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
        self.driver.get("http://localhost:8080/products/seeProducts")

    def test_create_products(self):
        '''
        # -- FUNÇÕES CRIADAS PARA TESTES --
        # create_product(self, nome, descrição, altura, largura, comprimento, peso
                        preçoDeCompra, categoria, grupoDePrecificação, preço, QuantidadeDisponível
                        dataDeEntrada, fornecedor, fabricante, tamanho, foto)
        '''
        
        with open('products.csv', mode='r', encoding='utf-8') as csvfile:
            reader = csv.DictReader(csvfile)

            for row in reader:
                create_product(self, row['Nome'], row['Descricao'], row['Altura'], row['Largura'], row['Comprimento'], row['Peso'], row['PrecoDeCompra'], row['Categoria'], row['GrupoDePrecificacao'], row['Preco'], row['QuantidadeDisponivel'], row['DataDeEntrada'], row['Fornecedor'], row['Fabricante'], row['Tamanho'], row['Foto'])
        
        # Espera alguns segundos para antes de fechar o browser.
        time.sleep(10)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
