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
        self.driver.get("http://localhost:8080/showAllClients")

    def test_create_10_credit_cards(self):

        # -- FUNÇÕES CRIADAS PARA TESTES --
        # click_button(self, id): Clica em botão da página.
        # input_string(self, id, valor): Insere texto.
        # select_radio(self, id, valor): Seleciona botão de rádio.
        # check_checkbox(self, id): Checha checkbox.
        # select_option(self, id, valor): Seleciona opção de um menu dropdown.
        # send_image(self, id, pasta, arquivo): Envia uma imagem.
        
        with open('creditCards.csv', mode='r', encoding='utf-8') as csvfile:
            reader = csv.DictReader(csvfile)

             # Iterando sobre as linhas do CSV
            for i, row in enumerate(reader, 1):  # i começa de 1
                if i > 10:  # Limita a leitura a 10 linhas
                    break

                # Itera o id do botão de ver os cartões de crédito do cliente
                see_button = "seeCreditCards-" + str(i)

                ## CRIAÇÃO DE CARTÃO DE CRÉDITO
                
                # Clica no botão de "Ver cartões"
                click_button(self, see_button)
                
                # Clica no botão "Adicionar cartão"
                click_button(self, "addCreditCardButton")

                # Preenche os campos do formulário de criar cartão de crédito.
                if row['Preferencial'] == "1":  # Verificando a coluna 'Preferencial'
                    check_checkbox(self, "preferential")

                # Preenche o número do cartão
                input_string(self, "cardNumber", row['Número do Cartão'])

                # Preenche o apelido
                input_string(self, "nickname", row['Apelido'])

                # Preenche o nome impresso
                input_string(self, "printedName", row['Nome Impresso'])

                # Seleciona a bandeira do cartão
                select_option(self, "brand", row['Bandeira'])

                # Preenche o CVV
                input_string(self, "securityCode", row['CVV'])

                # Clica no botão "Criar"
                click_button(self, "createButton")

                # Imprime no console
                print("Cartão de crédito do cliente de ID " + str(i) + " criado!")

        # CRIAÇÃO DO CARTÃO DE CRÉDITO PARA O CLIENTE DE ID=1 PARA FUNCIONAR TESTE DE COMPRA.
        # Clica no botão de "Ver cartões"
        click_button(self, "seeCreditCards-1")
        
        # Clica no botão "Adicionar cartão"
        click_button(self, "addCreditCardButton")

        # Preenche o número do cartão
        input_string(self, "cardNumber", "5497916374255558")

        # Preenche o apelido
        input_string(self, "nickname", "Itaú")

        # Preenche o nome impresso
        input_string(self, "printedName", "Otavio M")

        # Seleciona a bandeira do cartão
        select_option(self, "brand", "MasterCard")

        # Preenche o CVV
        input_string(self, "securityCode", "682")

        # Clica no botão "Criar"
        click_button(self, "createButton")

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
