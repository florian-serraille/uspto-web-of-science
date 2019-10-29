# USPTO DATA BASE

Configuração da imagem Docker do banco de dado MySQL para aplicação USPTO-APP. 

## Instalação

Se posicionar no diretório que o Dockerfile e executar o seguinte comando para gerar a imagem do banco.

```bash
docker build -t uspto-db .
```

## Criação de um container de dado persistente

Para armazenar os dados de banco de dado de maneira persistente e facilmente portatil, criamos um container data only com o segunite comando.  

```bash
docker create -v /var/lib/mysql --name dbdata busybox
```

## Instanciação de imagem do banco de dado
Segue o comando para gerar um container a partir da imagem do banco de dado, 

```bash
docker run --name uspto-db -d -p 3306:3306 --volumes-from dbdata uspto-db
```

O banco de dado está agora disponível localmente na porta 3306 para o usuário USPTO-APP com o password UCB.                                                                                                                                      


## Parar o container

```bash
docker stop uspto-db
```

## Reiniciar o container

```bash
docker start uspto-db
```

