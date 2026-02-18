import os
import unittest
import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.support.ui import Select
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait

# Função para a seleção de botão de rádio pelo ID.
def select_radio(self, radio_id, value):
    radio_button = self.driver.find_element(By.ID, radio_id)
    if radio_button.get_attribute("value") == value and not radio_button.is_selected():
        radio_button.click()
        
# Clica em um botão da página utilizando javascript.
def click_button(self, element_id):
    element = self.driver.find_element(By.ID, element_id)
    self.driver.execute_script("arguments[0].click();", element)

# Clica em "OK" de uma caixa de confirmação.
def accept_confirm_box(self):
    wait = WebDriverWait(self.driver, timeout=2)
    alert = wait.until(lambda d : d.switch_to.alert)
    text = alert.text
    alert.accept()

# Envia uma string em uma caixa de texto.
def input_string(self, element_id, value):
    self.driver.find_element(By.ID, element_id).send_keys(value)

# Função para apagar uma string de um campo.
def blank_field(self, element_id):
    self.driver.find_element(By.ID, element_id).clear()
    
# Checa uma checkbox (com javascript) e verifica se já está selecionada.
def check_checkbox(self, element_id):
    if not self.driver.find_element(By.ID, element_id).is_selected():
        element = self.driver.find_element(By.ID, element_id)
        self.driver.execute_script("arguments[0].click();", element)

# Seleciona uma opção visível de um menu dropdown.
def select_option(self, element_id, value):
    Select(self.driver.find_element(By.ID, element_id)).select_by_visible_text(value)

# Seleciona opção de menu dropdown pelo valor.
def select_option_by_value(self, element_id, value):
    Select(self.driver.find_element(By.ID, element_id)).select_by_value(value)

# Seleciona um produto da loja.
def select_product(self, product):
    card_bodies = self.driver.find_elements(By.CLASS_NAME, 'card-body')

    # Define o produto que será filtrado.
    product_to_select = product

    # Passa pelos card-bodies até achar o produto específico.
    for card_body in card_bodies:
        card_title = card_body.find_element(By.CLASS_NAME, 'card-title')
        if 'Preço' in card_title.text:  # Pula se for um card indesejado.
                continue
        
        if card_title.text == product_to_select:
            # Localiza o botão no mesmo card-body
            button = card_body.find_element(By.ID, 'seeProduct')

            time.sleep(0.5)      
            button.click()
            break

# Busca por uma imagem e manda ela.
def send_image(self, element_id, folder, image_file):
    # Pega o caminho absoluto do diretório do script
    script_dir = os.path.dirname(os.path.abspath(__file__))

    # Construindo o caminho absoluto para o arquivo de imagem dentro da pasta de Imagens.
    file_path = os.path.join(script_dir, folder, image_file)

    # Garantir que o caminho do arquivo está certo.
    print("Caminho do arquivo: ", file_path)

    # Checar se o arquivo existe.
    if not os.path.exists(file_path):
        print("O arquivo não existe!")
    else:
        # Localizar o <input> do tipo file por seu ID e fazer o upload do arquivo.
        self.driver.find_element(By.ID, element_id).send_keys(file_path)

# Cria um cliente de forma automatizada (não contém endereço).
def create_client(self, name, email, password, birthday, cpf, enabled, gender):
    click_button(self, "createClientButton")
        
    input_string(self, "name", name)

    input_string(self, "email", email)
    
    input_string(self, "password", password)
    
    input_string(self, "confirmPassword", password)

    # Formato de data: mm-dd-yyyy
    input_string(self, "birthday", birthday)
    
    input_string(self, "cpf", cpf)
    
    if enabled == "1":
        check_checkbox(self, "enabled")
        
    # M = 1, F = 2, ? = 3
    if gender == "1":
        select_radio(self, "gender-1", "1")
        
    if gender == "2":
        select_radio(self, "gender-2", "2")
        
    if gender == "3":
        select_radio(self, "gender-3", "3")

# Cria telefone de forma automatizada.
def create_phone(self, landline, ddd, phoneNumber):
    if landline == "1":
        select_radio(self, "phoneFalse", "false")
    else:
        select_radio(self, "phoneTrue", "true")
        
    input_string(self, "ddd", ddd)

    input_string(self, "phoneNumber", phoneNumber)

# Cria endereço de na página de criação de cliente de forma automatizada.
def create_address(self, delivery, charging, nickname, typeOfResidence, addressingType, addressing, houseNumber, cep, neighbourhood, city, state, observation):
    if delivery == "1":
        check_checkbox(self, "deliveryAddress")
    if charging == "1":
        check_checkbox(self, "chargingAddress")

    input_string(self, "nickname", nickname)

    input_string(self, "typeOfResidence", typeOfResidence)

    input_string(self, "addressingType", addressingType)

    input_string(self, "addressing", addressing)

    input_string(self, "houseNumber", houseNumber)

    input_string(self, "cep", cep)

    input_string(self, "neighbourhood", neighbourhood)

    input_string(self, "city", city)
    
    select_option(self, "stateSelect", state)

    select_option(self, "countrySelect", "Brasil")

    input_string(self, "observation", observation)
    
    click_button(self, "createButton")

# Cria produtos de forma automatizada.
def create_product(self, name, description, height, width, length, weight, purchaseAmount, category, pricingGroup, price, quantityAvailable, entryDate, supplier, manufacturer, size, fileInput):
    click_button(self, "createProduct")
        
    input_string(self, "name", name)

    input_string(self, "description", description)
    
    input_string(self, "height", height)
    
    input_string(self, "width", width)
    
    input_string(self, "length", length)
    
    input_string(self, "weight", weight)
    
    input_string(self, "purchaseAmount", purchaseAmount)
    
    select_option(self, "categorySelect", category)
    
    select_option(self, "pricingGroupSelect", pricingGroup)

    input_string(self, "price", price)

    input_string(self, "productQuantityAvailable", quantityAvailable)

    input_string(self, "entryInStockDate", entryDate)
    
    select_option(self, "supplier", supplier)
    
    select_option(self, "manufacturer", manufacturer)
    
    select_option(self, "size", size)

    send_image(self, "fileInput", "Images", fileInput)
    
    click_button(self, "createButton")

# Cria produtos de forma automatizada.
def buy_product(self, product_name, quantity):
    
    # Scrollando a página pra ficar mais fácil do Selenium achar os produtos.
    self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight)")  # Scroll to bottom
    # Adicionando o produto no carrinho.
    select_product(self, product_name)
    input_string(self, "quantity", quantity)
    
    click_button(self, "addToCart")

    click_button(self, "backToShop")


#if __name__ == "__main__":
#   unittest.main()
