![](https://imgur.com/xkGeyhw.gif)

# Sistema de Locadora de Filmes

Este é um sistema de desktop para gerenciamento de uma locadora de filmes, desenvolvido em Java com a interface gráfica construída em Swing. Ele permite o controle completo sobre o catálogo de filmes (físicos e de streaming), o cadastro de clientes e o registro de locações.

## ✨ Funcionalidades

O sistema oferece as seguintes funcionalidades:

- **Gerenciamento de Filmes:**
    - Cadastrar novos filmes, tanto físicos (com número de disco e tipo de mídia) quanto de streaming (com plataforma e link).
    - Listar todos os filmes cadastrados com seus detalhes.
    - Atualizar informações de filmes existentes.
    - Remover filmes do catálogo.
- **Gerenciamento de Clientes:**
    - Cadastrar novos clientes com nome, CPF, telefone e email.
    - Listar todos os clientes.
    - Atualizar informações de clientes (CPF não é editável).
    - Remover clientes do sistema.
- **Gerenciamento de Locações:**
    - Registrar uma nova locação para um cliente, selecionando um filme disponível.
    - Dar baixa (devolver) em uma locação ativa.
    - Listar todas as locações ativas, com destaque para as atrasadas.
    - Consultar o histórico completo de locações.
    - Consultar o histórico de locações de um cliente específico.
- **Consultas e Manutenção:**
    - Listar todos os filmes disponíveis para locação.
    - Verificar a integridade do banco de dados e aplicar correções na estrutura das tabelas automaticamente.

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java
- **Interface Gráfica:** Java Swing
- **Banco de Dados:** MySQL
- **Conectividade com o Banco:** JDBC (com o driver MySQL Connector/J)

## 🚀 Como Executar o Projeto

Siga os passos abaixo para configurar e executar o projeto em seu ambiente local.

### Pré-requisitos

- **JDK 11** ou superior instalado e configurado.
- **MySQL Server** instalado e em execução.
- Um **cliente de banco de dados** (como DBeaver, HeidiSQL ou o próprio MySQL Workbench) para criar o banco e executar o script.

### 1. Clonar o Repositório

```bash
git clone <https://github.com/Ledemarxavier/SistemaLocadora>
cd <SistemaLocadora>
```

### 2. Configurar o Banco de Dados

1.  Abra seu cliente de banco de dados e conecte-se ao seu servidor MySQL.
2.  Crie um novo banco de dados chamado `locadora_filmes`.
    ```sql
    CREATE DATABASE IF NOT EXISTS locadora_filmes;
    ```
3.  Execute o script SQL localizado em `SistemaLocadora/src/scripts/criar_banco.sql` para criar todas as tabelas e índices necessários.

### 3. Configurar a Conexão

1.  Abra o arquivo `SistemaLocadora/src/dao/ConexaoDAO.java`.
2.  Altere as credenciais de conexão (`URL`, `USER`, `PASSWORD`) de acordo com a sua configuração do MySQL.

    ```java
    public class ConexaoDAO {
        private static final String URL = "jdbc:mysql://localhost:3306/locadora_filmes";
        private static final String USER = "seu_usuario";      
        private static final String PASSWORD = "sua_senha";    
        // ...
    }
    ```

### 4. Adicionar o Driver JDBC

1.  Crie uma pasta chamada `lib` dentro do diretório `SistemaLocadora`.
2.  Faça o download do **MySQL Connector/J** no site oficial da Oracle/MySQL.
3.  Descompacte o arquivo e copie o arquivo `.jar` (ex: `mysql-connector-j-8.x.xx.jar`) para dentro da pasta `lib` que você criou.

### 5. Compilar e Executar

Abra um terminal na raiz do projeto e execute os seguintes comandos.

**No Windows (usando PowerShell ou CMD):**

```bash
# Navegue até a pasta do sistema
cd SistemaLocadora

# Compile o projeto (substitua pelo nome exato do seu .jar)
javac -cp "src;lib/mysql-connector-j-8.0.33.jar" src/view/MenuPrincipal.java

# Execute a aplicação
java -cp "src;lib/mysql-connector-j-8.0.33.jar" view.MenuPrincipal
```

**No Linux ou macOS:**

```bash
# Navegue até a pasta do sistema
cd SistemaLocadora

# Compile o projeto (substitua pelo nome exato do seu .jar)
javac -cp "src:lib/mysql-connector-j-8.0.33.jar" src/view/MenuPrincipal.java

# Execute a aplicação
java -cp "src:lib/mysql-connector-j-8.0.33.jar" view.MenuPrincipal
```

> **Nota:** Certifique-se de usar o nome exato do arquivo `.jar` do MySQL Connector/J que você baixou.

## 📂 Estrutura do Projeto

O projeto segue uma arquitetura em camadas para separar as responsabilidades:

- `src/dao`: **Data Access Objects** - Classes responsáveis pela comunicação direta com o banco de dados (operações de CRUD).
- `src/model`: **Modelos** - Classes que representam as entidades do sistema (Cliente, Filme, Locacao).
- `src/service`: **Serviços** - Classes que contêm a lógica de negócio da aplicação.
- `src/view`: **Visão** - Classes responsáveis pela interface gráfica com o usuário (janelas e componentes Swing).
- `src/scripts`: Contém os scripts SQL para a criação e manutenção do banco de dados.
- `lib/`: Contém as bibliotecas de terceiros (drivers JDBC, etc.).
