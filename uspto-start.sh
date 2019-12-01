#!/usr/bin/env bash

# Set variables
DOCKER_COMPOSE=USPTO-WEB-OF-SCIENCE/docker-compose.yml
USPTO="${USPTO}"

# Colors
RED='\e[0;31m'
BLUE='\e[1;34m'
GREEN='\e[1;32m'
BASIC='\e[0;m'

export_var() {
  echo -e "\n# USPTO ROOT DIRECTORY" >>~/.bash_profile
  echo "USPTO=${USPTO}" >>~/.bash_profile
  echo -e "${GREEN}Variavel USPTO adicionada ao arquivo '$(HOME)/.bash_profile'${BASIC}"
  source ~/.bash_profile
}

is_valid_directory() {

  if [[ ! -d "${USPTO}" ]]; then
    echo -e "${RED}'${USPTO}' não é um diretório válido ${BASIC}" >&2
    exit 1
  elif [[ ! -e ${USPTO}/${DOCKER_COMPOSE} ]]; then
    echo -e "${RED}Arquivo ${DOCKER_COMPOSE} não encontrado em '${USPTO}/${DOCKER_COMPOSE}'${BASIC}" >&2
    exit 1
  fi
}

ask_for_directory() {
  echo -e "${RED}Variável de ambiente USPTO não configurada${BASIC}"
  echo "Favor indicar o caminho da diretorio raíz da aplicação:"
  read USPTO
}

is_uspto_var_present() {

  [[ -e ~/.bash_profile ]] && source ~/.bash_profile
  if [[ -z "${USPTO}" ]]; then
    ask_for_directory
    is_valid_directory "${USPTO}"
    export_var
  else
    is_valid_directory "${USPTO}"
  fi
}

print_header() {
  echo -e "${BLUE}"
  echo '
__/\\\________/\\\________/\\\\\\\\\__/\\\\\\\\\\\\\___
 _\/\\\_______\/\\\_____/\\\////////__\/\\\/////////\\\_
  _\/\\\_______\/\\\___/\\\/___________\/\\\_______\/\\\_
   _\/\\\_______\/\\\__/\\\_____________\/\\\\\\\\\\\\\\__
    _\/\\\_______\/\\\_\/\\\_____________\/\\\/////////\\\_
     _\/\\\_______\/\\\_\//\\\____________\/\\\_______\/\\\_
      _\//\\\______/\\\___\///\\\__________\/\\\_______\/\\\_
       __\///\\\\\\\\\/______\////\\\\\\\\\_\/\\\\\\\\\\\\\/__
        ____\/////////___________\/////////__\/////////////____
'
  echo -e ${BASIC}
}

start_app() {
  export "USPTO=${USPTO}"
  docker-compose -f "${USPTO}/${DOCKER_COMPOSE}" up -d
}

print_header
is_uspto_var_present
start_app
