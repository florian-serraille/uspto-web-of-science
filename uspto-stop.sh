#!/usr/bin/env bash

# Set variables
DOCKER_COMPOSE=USPTO-WEB-OF-SCIENCE/docker-compose.yml
USPTO="${USPTO}"

# Colors
RED='\e[0;31m'
BLUE='\e[1;34m'
BASIC='\e[0;m'

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

import_var() {
  if [[ -e ~/.bash_profile ]]; then
    source ~/.bash_profile
  else
    echo -e "${RED}Variavel de ambiente USPTO não encontrada${BASIC}" >&2
    exit 1
  fi
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

stop_app(){
    export "USPTO=${USPTO}"
    docker-compose -f "${USPTO}/${DOCKER_COMPOSE}" stop
}

print_header
import_var
is_valid_directory "${USPTO}"
stop_app