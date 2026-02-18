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

    def test_update_address(self):

        # Clica no botão de "Ver endereços".
        click_button(self, "seeAddresses-1")
        time.sleep(1)
        
        # Clica no botão de colapsar.
        click_button(self, "addressCollapse-12")
        time.sleep(1)

        # Clica no botão de "Atualizar".
        click_button(self, "updateAddress-12")
        time.sleep(1)
        
        # Preenche os campos do formulário de criar endereço.
        check_checkbox(self, "deliveryAddress")
        time.sleep(1)
        
        blank_field(self, "nickname")
        input_string(self, "nickname", "Casa de Praia")
        time.sleep(1)

        blank_field(self, "typeOfResidence")
        input_string(self, "typeOfResidence", "Casa")
        time.sleep(1)

        blank_field(self, "addressingType")
        input_string(self, "addressingType", "Rua")
        time.sleep(1)

        blank_field(self, "addressing")
        input_string(self, "addressing", "CN")
        time.sleep(1)

        blank_field(self, "houseNumber")
        input_string(self, "houseNumber", "42")
        time.sleep(1)

        blank_field(self, "cep")
        input_string(self, "cep", "11250-000")
        time.sleep(1)

        blank_field(self, "neighbourhood")
        input_string(self, "neighbourhood", "Costa do Sol")
        time.sleep(1)

        blank_field(self, "city")
        input_string(self, "city", "Bertioga")
        time.sleep(1)

        #select_option(self, "stateSelect", "São Paulo")
        #time.sleep(1)

        #select_option(self, "countrySelect", "Brasil")
        #time.sleep(1)

        blank_field(self, "observation")
        input_string(self, "observation", "De frente a praia.")
        time.sleep(1)

        click_button(self, "updateAddress")
        time.sleep(1)
        
        # Espera alguns segundos para o resultado.
        time.sleep(4)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
