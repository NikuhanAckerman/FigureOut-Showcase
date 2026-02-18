import sys
import os

sys.path.insert(0, os.path.dirname(os.getcwd()))

import unittest
from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
import time
from ChromeSeleniumFunctions import *

class ClientTest(unittest.TestCase):
    def setUp(self):
        # Configura as opções do Chrome
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")

        # Iniciliza o WebDriver
        self.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
        self.driver.get("http://localhost:8080/showAllClients")

    def test_update_credit_card(self):
      
        ## ATUALIZAÇÃO DE ENDEREÇO
        # Clica no botão de "Ver endereços".
        click_button(self, "seeCreditCards-1")
        time.sleep(1)
        
        # Clica no botão de colapsar.
        click_button(self, "creditCardCollapse-12")
        time.sleep(1)

        # Clica no botão de "Atualizar".
        click_button(self, "updateCreditCard-12")
        time.sleep(1)
        
        # Preenche os campos do formulário de criar cartão de crédito.
        blank_field(self, "cardNumber")
        input_string(self, "cardNumber", "5554786883472238")
        time.sleep(1)
        
        blank_field(self, "nickname")
        input_string(self, "nickname", "Cartao Azul")
        time.sleep(1)

        blank_field(self, "printedName")
        input_string(self, "printedName", "O Monteiro")
        time.sleep(1)
        
        select_option(self, "brand", "Visa")
        time.sleep(1)

        blank_field(self, "securityCode")
        input_string(self, "securityCode", "154")
        time.sleep(1)

        click_button(self, "updateCreditCard")
        
        # Espera alguns segundos para o resultado.
        time.sleep(4)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
