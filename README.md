# Assessment - Domain-Driven Design (DDD) e Arquitetura de Softwares Escaláveis com Java

## 1. Event Storm

### 1.1. Tema
Sistema de Gerenciamento de Biblioteca Digital

### 1.2. Domínios
Identificamos três domínios principais para o Sistema de Gerenciamento de Biblioteca Digital: Gerenciamento de Usuários, Catálogo de Livros e Empréstimos e Devoluções.

![image](https://github.com/user-attachments/assets/829ef8fc-5210-43a0-a191-bf1d01136fe9)

Este gráfico é dividido em três seções principais, cada uma representando um domínio identificado. Os eventos estão representados por retângulos laranja claro, os comandos por retângulos azulo claro, e os domínios pelos retângulos maiores que englobam os eventos e comandos.

Os eventos representam fatos ocorridos no sistema que podem desencadear outras ações. Os comandos representam ações que podem ser executadas pelos usuários ou por outras partes do sistema.

Cada um desses domínios poderia ser implementado como um microsserviço separado, com sua própria base de dados e lógica de negócios. Eles se comunicariam entre si através de eventos e APIs bem definidas.

### 1.3 Quadro de identificação

| Aspecto | Descrição |
|---------|-----------|
| **Envolvidos** | 1. Usuários da biblioteca (estudantes, professores, pesquisadores)<br>2. Bibliotecários e administradores do sistema<br>3. Equipe de TI responsável pela manutenção do sistema<br>4. Gestores da instituição (universidade, escola, empresa) |
| **Problemas ou Oportunidades** | 1. Acesso limitado a recursos físicos da biblioteca<br>2. Dificuldade em gerenciar grande volume de empréstimos e devoluções<br>3. Falta de dados analíticos sobre o uso do acervo<br>4. Oportunidade de expandir o alcance da biblioteca além das limitações físicas<br>5. Possibilidade de oferecer recursos multimídia e interativos |
| **Impacto** | 1. Aumento no acesso a recursos educacionais e de pesquisa<br>2. Redução de custos operacionais da biblioteca<br>3. Melhoria na experiência do usuário<br>4. Aumento na eficiência dos processos de gestão da biblioteca<br>5. Possibilidade de colaboração e compartilhamento de conhecimento em escala global |
| **Necessidade** | 1. Sistema robusto e escalável para gerenciar grande volume de conteúdo digital<br>2. Interface intuitiva para usuários e administradores<br>3. Funcionalidades de busca avançada e recomendação de conteúdo<br>4. Integração com sistemas de autenticação existentes<br>5. Capacidade de gerar relatórios e análises de uso |
| **Quem ou O Que** | 1. Quem: Equipe de desenvolvimento de software<br>2. O Que: Plataforma de gerenciamento de biblioteca digital baseada em microsserviços<br>3. Quem: Especialistas em UX/UI para design da interface<br>4. O Que: Infraestrutura de nuvem para hospedar o sistema<br>5. Quem: Especialistas em segurança da informação para garantir a proteção dos dados |

### 1.4 Fluxos de ação dos domínios
![image](https://github.com/user-attachments/assets/689af966-b488-4593-928e-155068f1771f)

## 2. Criação do domínio

Foram criados três microsserviços principais para representar os domínios identificados:

### 2.1. Microsserviço de Gerenciamento de Usuários (usermanagement)

Este microsserviço é responsável pelo gerenciamento de usuários do sistema.

### Estrutura principal:

- **Modelo**: `Usuario.java`
- **Repositório**: `UsuarioRepository.java`
- **Controlador**: `UsuarioController.java`

### Fluxos implementados:

1. Registro de novo usuário
2. Atualização de perfil de usuário
3. Busca de usuários
4. Deleção de usuário

Exemplo de implementação (UsuarioController.java):

```java
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody Usuario usuario) {
        // Implementação do registro de usuário
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarPerfil(@PathVariable("id") long id, @Valid @RequestBody Usuario usuario) {
        // Implementação da atualização de perfil
    }

    // Outros métodos...
}
```

### 2.2. Microsserviço de Gerenciamento de Livros (bookmanagement)

Este microsserviço lida com o catálogo de livros da biblioteca digital.

### Estrutura principal:

- **Modelo**: `Livro.java`
- **Repositório**: `LivroRepository.java`
- **Controlador**: `LivroController.java`

### Fluxos implementados:

1. Adição de novo livro
2. Atualização de informações do livro
3. Busca de livros
4. Remoção de livro

Exemplo de implementação (LivroController.java):

```java
@RestController
@RequestMapping("/api/livros")
public class LivroController {
    @PostMapping
    public ResponseEntity<Livro> criarLivro(@Valid @RequestBody Livro livro) {
        // Implementação da criação de livro
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> getLivroById(@PathVariable("id") long id) {
        // Implementação da busca de livro por ID
    }

    // Outros métodos...
}
```

### 2.3. Microsserviço de Sistema de Empréstimos (loansystem)

Este microsserviço gerencia os empréstimos de livros aos usuários.

### Estrutura principal:

- **Modelo**: `Emprestimo.java`
- **Repositório**: `EmprestimoRepository.java`
- **Controlador**: `EmprestimoController.java`
- **Serviço**: `BookService.java`

### Fluxos implementados:

1. Realização de empréstimo
2. Registro de devolução
3. Busca de empréstimos
4. Listagem de empréstimos por usuário ou livro

Exemplo de implementação (EmprestimoController.java):

```java
@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {
    @PostMapping
    public ResponseEntity<Emprestimo> realizarEmprestimo(@Valid @RequestBody Emprestimo emprestimo) {
        // Implementação da realização de empréstimo
    }

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<Emprestimo> registrarDevolucao(@PathVariable("id") long id) {
        // Implementação do registro de devolução
    }

    // Outros métodos...
}
```

### Características Comuns dos Microsserviços

Todos os três microsserviços compartilham algumas características comuns, alinhadas com o archetype mencionado:

1. **Uso do Spring Boot**: Todos os microsserviços são aplicações Spring Boot, facilitando a configuração e o deployment.

2. **Banco de Dados H2**: Utilizam o banco de dados H2 para armazenamento de dados, ideal para desenvolvimento e testes.

3. **Spring Data JPA**: Para persistência e acesso a dados.

4. **Validação**: Uso de anotações de validação do Jakarta Validation.

5. **Swagger/OpenAPI**: Documentação da API usando SpringDoc OpenAPI.

6. **Actuator**: Endpoints de monitoramento e health check.

## 3. Camada de comunicação síncrona

### 3.1 Camada de Serviço Responsável pela Chamada REST

No microsserviço `loansystem`, foi implementada uma camada de serviço responsável por realizar chamadas REST síncronas. Esta implementação pode ser encontrada no arquivo `BookService.java`:

```java
@Service
public class BookService {
    private final BookClient bookClient;

    public BookService(BookClient bookClient) {
        this.bookClient = bookClient;
    }

    public LivroDTO getLivroById(Long id) {
        return bookClient.getLivroById(id);
    }
}
```

### 3.2 Implementação da Comunicação com Spring Feign

A comunicação síncrona foi implementada utilizando o Spring Cloud OpenFeign. A interface `BookClient` no microsserviço `loansystem` demonstra esta implementação:

```java
@FeignClient(name = "bookmanagement", url = "http://localhost:8082")
public interface BookClient {
    @GetMapping("/api/livros/{id}")
    LivroDTO getLivroById(@PathVariable("id") Long id);
}
```

Esta interface é utilizada para fazer chamadas HTTP GET síncronas ao microsserviço `bookmanagement` para recuperar informações de livros.

## 4. Camada de comunicação assíncrona

### 4.1 Instalação do RabbitMQ

O RabbitMQ foi configurado para ser executado como um contêiner Docker, como pode ser visto no arquivo `docker-compose.yml`:

```yaml
services:
  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      - RABBITMQ_DEFAULT_USER=admin
      - RABBITMQ_DEFAULT_PASS=adminpassword
```

### 4.2 Configuração do RabbitMQ nos Microsserviços

A configuração do RabbitMQ foi implementada em ambos os microsserviços `usermanagement` e `loansystem`. Exemplo do arquivo `RabbitMQConfig.java` no microsserviço `usermanagement`:

```java
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue userQueue() {
        return new Queue("userQueue", true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // ... outros beans e configurações
}
```

### 4.3 Envio de JSON para Fila do RabbitMQ

No microsserviço `usermanagement`, o envio de mensagens JSON para o RabbitMQ é implementado no `UsuarioController`:

```java
@PostMapping("/registro")
public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody Usuario usuario) {
    // ... lógica de registro do usuário

    String usuarioJson = objectMapper.writeValueAsString(_usuario);
    RabbitTemplate tracedRabbitTemplate = springRabbitTracing.newRabbitTemplate(connectionFactory);
    tracedRabbitTemplate.convertAndSend(userQueue.getName(), usuarioJson);

    // ... retorno da resposta
}
```

### 4.4 Consumo de JSON do RabbitMQ

No microsserviço `loansystem`, o consumo de mensagens JSON do RabbitMQ é implementado no `UserMessageService`:

```java
@Service
public class UserMessageService {
    @RabbitListener(queues = "userQueue", containerFactory = "tracingRabbitListenerContainerFactory")
    public void receiveUserMessage(String message) throws JsonProcessingException {
        UserInfo userInfo = objectMapper.readValue(message, UserInfo.class);
        userInfoRepository.save(userInfo);
    }
}
```

### 4.5 Gravação dos Dados no Banco de Dados

Após o consumo da mensagem, os dados são salvos no banco de dados usando o `UserInfoRepository`:

```java
userInfoRepository.save(userInfo);
```

## 5. Camada de Observabilidade
### 5.1 Implementação de Logs para Rastreabilidade

Logs detalhados foram implementados em vários pontos dos microsserviços. Por exemplo, no `UsuarioController` do microsserviço `usermanagement`:

```java
logger.info("Recebida solicitação para registrar usuário: {}", usuario.getEmail());
// ... outras linhas de log
```

### 5.2 Implementação de Traces e SpanId com Zipkin e Micrometer

A configuração para traces e SpanId usando Zipkin e Micrometer foi implementada nos arquivos `application.properties` dos microsserviços:

```properties
management.tracing.sampling.probability=1.0
management.zipkin.tracing.endpoint=http://localhost:9411/api/v2/spans

logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]
```

Além disso, as dependências necessárias foram adicionadas aos arquivos `pom.xml`:

```xml
<dependency>
    <groupId>io.zipkin.reporter2</groupId>
    <artifactId>zipkin-reporter-brave</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>
```

### 5.3 Configuração do RabbitMQ para Tracing Distribuído

A configuração do RabbitMQ nos microsserviços foi estendida para suportar o tracing distribuído usando Zipkin. Isso permite que as mensagens assíncronas trocadas entre os serviços sejam rastreadas como parte de uma transação distribuída.

![image](https://github.com/user-attachments/assets/df3fc7fa-c7bd-4b40-a067-716d7448a612)


#### Arquivo RabbitMQConfig

O arquivo `RabbitMQConfig.java`, presente nos microsserviços `usermanagement` e `loansystem`, foi configurado para integrar o RabbitMQ com o Zipkin para tracing distribuído. Aqui está uma análise detalhada da configuração:

```java
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue userQueue() {
        return new Queue("userQueue", true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SpringRabbitTracing springRabbitTracing(Tracing tracing) {
        return SpringRabbitTracing.newBuilder(tracing).build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory tracingRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            SpringRabbitTracing springRabbitTracing,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return springRabbitTracing.decorateSimpleRabbitListenerContainerFactory(factory);
    }
}
```

#### Componentes Chave:

1. **Queue Bean**: Define a fila "userQueue" que será usada para a comunicação assíncrona.

2. **MessageConverter Bean**: Configura um conversor JSON para serializar/desserializar mensagens.

3. **SpringRabbitTracing Bean**: Este é um componente crucial para a integração com Zipkin. Ele cria um bean `SpringRabbitTracing` que será usado para decorar as operações do RabbitMQ com informações de tracing.

4. **tracingRabbitListenerContainerFactory Bean**: Este factory é configurado com o `SpringRabbitTracing` para garantir que os listeners do RabbitMQ sejam instrumentados para tracing. Isso significa que quando uma mensagem é consumida, ela será automaticamente incluída no contexto de tracing atual.

#### Impacto na Observabilidade

Esta configuração tem um impacto significativo na observabilidade do sistema:

1. **Tracing End-to-End**: Permite o rastreamento de transações que envolvem comunicação assíncrona entre serviços. Por exemplo, quando um usuário é registrado no `usermanagement` e uma mensagem é enviada para o `loansystem`, todo esse fluxo pode ser rastreado como uma única transação no Zipkin.

2. **Correlação de Eventos**: Facilita a correlação de eventos entre diferentes serviços, mesmo quando a comunicação é assíncrona.

3. **Diagnóstico de Problemas**: Torna mais fácil diagnosticar problemas em fluxos complexos que envolvem múltiplos serviços e comunicação assíncrona.

4. **Visibilidade de Latência**: Permite visualizar a latência introduzida pela comunicação assíncrona no fluxo geral da aplicação.

## 6. Conclusão
Em conclusão, o projeto de Sistema de Gerenciamento de Biblioteca Digital demonstra uma abordagem para o desenvolvimento de uma arquitetura de microsserviços. 
Começando com um Event Storm que identificou os domínios principais, o projeto evoluiu para a implementação de três microsserviços distintos: Gerenciamento de Usuários, 
Catálogo de Livros e Sistema de Empréstimos. Cada microsserviço foi construído com uma arquitetura robusta, utilizando Spring Boot e seguindo as melhores práticas de desenvolvimento. 
A implementação incluiu comunicação síncrona através do Spring Cloud OpenFeign e comunicação assíncrona utilizando RabbitMQ, proporcionando flexibilidade e escalabilidade ao sistema. 
Além disso, a integração de uma camada de observabilidade com Zipkin e Micrometer, juntamente com a configuração do RabbitMQ para tracing distribuído, permite um monitoramento eficaz 
e facilita a manutenção e o diagnóstico de problemas em um ambiente distribuído complexo.
Este projeto serve como um bom exemplo de como princípios de Domain-Driven Design podem ser aplicados na prática para criar um sistema robusto, escalável e observável.
