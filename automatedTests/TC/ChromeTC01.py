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
        # Configura as opções do Chrome.
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")

        # Iniciliza o WebDriver.
        self.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
        self.driver.get("http://localhost:8080/showAllClients")

    def test_create_client(self):

        click_button(self, "createClientButton")
        
        input_string(self, "name", "Renan Luiz")
        time.sleep(1)

        input_string(self, "email", "renan@protonmail.com")
        time.sleep(1)
        
        input_string(self, "password", "senhaForte123@")
        time.sleep(1)
        
        input_string(self, "confirmPassword", "senhaForte123@")
        time.sleep(1)
        
        input_string(self, "birthday", "07-10-2003")
        time.sleep(1)
        
        input_string(self, "cpf", "521.193.488-06")
        time.sleep(1)

        check_checkbox(self, "enabled")
        time.sleep(1)

        select_radio(self, "gender-1", "1")
        time.sleep(1)

        # Informações de Telefone.
        select_radio(self, "phoneFalse", "false")
        time.sleep(1)

        input_string(self, "ddd", "11")
        time.sleep(1)

        input_string(self, "phoneNumber", "95121234")
        time.sleep(1)

        # Informações de endereço.
        check_checkbox(self, "deliveryAddress")
        time.sleep(1)

        check_checkbox(self, "chargingAddress")
        time.sleep(1)

        input_string(self, "nickname", "Minha casa")
        time.sleep(1)

        input_string(self, "typeOfResidence", "Casa")
        time.sleep(1)

        input_string(self, "addressingType", "Rua")
        time.sleep(1)

        input_string(self, "addressing", "Jardelina")
        time.sleep(1)

        input_string(self, "houseNumber", "750")
        time.sleep(1)

        input_string(self, "cep", "08730-300")
        time.sleep(1)

        input_string(self, "neighbourhood", "Parque Santana")
        time.sleep(1)

        input_string(self, "city", "Mogi das Cruzes")
        time.sleep(1)
        
        select_option(self, "stateSelect", "São Paulo")
        time.sleep(1)

        select_option(self, "countrySelect", "Brasil")
        time.sleep(1)

        input_string(self, "observation", "Próximo do supermercado Nagumo.")
        time.sleep(1)
        
        click_button(self, "createButton")
        time.sleep(1)

        # Espera alguns segundos para antes de fechar o browser.
        time.sleep(3)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
