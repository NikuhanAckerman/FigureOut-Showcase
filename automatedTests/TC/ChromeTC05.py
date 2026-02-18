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

    def test_create_address(self):

        click_button(self, "seeAddresses-1")
        time.sleep(1)
        
        # Clica no botão "Adicionar endereço"
        click_button(self, "createAddress")
        time.sleep(1)
        
        # Preenche os campos do formulário de criar endereço.
        check_checkbox(self, "deliveryAddress")
        time.sleep(1)

        input_string(self, "nickname", "Casa de Férias")
        time.sleep(1)

        input_string(self, "typeOfResidence", "Casa")
        time.sleep(1)

        input_string(self, "addressingType", "Avenida")
        time.sleep(1)

        input_string(self, "addressing", "das Orquídeas")
        time.sleep(1)

        input_string(self, "houseNumber", "765")
        time.sleep(1)

        input_string(self, "cep", "04567-305")
        time.sleep(1)

        input_string(self, "neighbourhood", "Centro")
        time.sleep(1)

        input_string(self, "city", "Poá")
        time.sleep(1)
        
        select_option(self, "stateSelect", "São Paulo")
        time.sleep(1)

        select_option(self, "countrySelect", "Brasil")
        time.sleep(1)

        input_string(self, "observation", "Próximo do balneário.")
        time.sleep(1)
        
        click_button(self, "createButton")
        time.sleep(1)
        
        # Espera alguns segundos para o resultado.
        time.sleep(4)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
