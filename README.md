# Cloud Notes 🗒️ 

<!-- Opción 2: ancho máximo responsivo -->
<p align="center">
  <img src="readmeImgs/cloudPort.png" alt="cloudNotes" style="max-width:500px; width:100%; height:auto;" />
</p>

Cloud Notes es una aplicación de gestión de notas que permite a los usuarios crear, leer, actualizar y eliminar notas almacenadas en la nube utilizando servicios simulados de AWS a través de LocalStack.
Este proyecto surge por el interés de aprender a integrar servicios en la nube en aplicaciones Java, específicamente utilizando LocalStack para simular un entorno de AWS localmente.

## DynamoDB

DynamoDB es un servicio de base de datos NoSQL totalmente gestionado que ofrece un rendimiento rápido y predecible con escalabilidad automática. En esta aplicación, DynamoDB se utiliza para almacenar y gestionar las notas creadas por los usuarios.

Se está trabajando con DynamoDb ya que ofrece:
- alta velocidad,
- lecturas rápidas,
- escalabilidad horizontal,
- documentos flexibles,
- esquema no rígido,
- integración con AWS (S3, Lambdas, API Gateway, etc).

## Comandos LocalStack

Comandos solo para localStack.

- Arrancar LocalStack:

```
localstack start
```

## Comandos LocalStack - DynamoDB

Estos son algunos comandos utilies que usé para trabajar con LocalStack y DynamoDB:


- Crear tabla:


```
aws dynamodb create-table \
  --endpoint-url http://localhost:4566 \
  --table-name notes \
  --attribute-definitions AttributeName=noteId,AttributeType=S \
  --key-schema AttributeName=noteId,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST
```

- Verificar tablas creadas:

```
aws dynamodb list-tables --endpoint-url http://localhost:4566
```

- Validar datos en tablas

```
aws dynamodb scan --table-name notes --endpoint-url http://localhost:4566
```
O
```
aws dynamodb scan \
  --table-name notes \
  --endpoint-url http://localhost:4566
```

- crear tabla para los archivos


```
aws dynamodb create-table \
  --table-name NoteAttachments \
  --attribute-definitions \
      AttributeName=noteAttachmentId,AttributeType=S \
      AttributeName=noteId,AttributeType=S \
  --key-schema \
      AttributeName=noteAttachmentId,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST \
  --global-secondary-indexes \
      "[
          {
              \"IndexName\": \"noteId-index\",
              \"KeySchema\": [
                  {\"AttributeName\": \"noteId\", \"KeyType\": \"HASH\"}
              ],
              \"Projection\": {\"ProjectionType\": \"ALL\"},
              \"ProvisionedThroughput\": {
                  \"ReadCapacityUnits\": 5,
                  \"WriteCapacityUnits\": 5
              }
          }
      ]" \
  --endpoint-url=http://localhost:4566
```

#### Tener en cuenta para crear tablas
| Parte                            | Función                           |
| -------------------------------- | --------------------------------- |
| `--table-name NoteAttachments`   | Nombre de la tabla                |
| `--attribute-definitions`        | Atributos usados en índices       |
| `--key-schema`                   | Clave primaria (noteAttachmentId) |
| `--billing-mode PAY_PER_REQUEST` | Modo de cobro                     |
| `--global-secondary-indexes`     | Índice para buscar por noteId     |
| `--endpoint-url`                 | Usar LocalStack                   |


## S3
S3 (Simple Storage Service) es un servicio de almacenamiento de objetos en la nube que permite almacenar y recuperar cualquier cantidad de datos desde cualquier lugar en la web. En esta aplicación, S3 se utiliza para almacenar archivos adjuntos asociados a las notas creadas por los usuarios.

Este sistema usa s3 para almacenar los archivos adjuntos de las notas. Los archivos se suben a un bucket de S3 y se asocian con las notas mediante identificadores únicos.

### Comandos LocalStack - S3

- Crear bucket:

```
aws --endpoint-url=http://localhost:4566 s3 mb s3://cloudnotes-bucket
```

- Verificar que bucket existen:

```
aws --endpoint-url=http://localhost:4566 s3 ls
```

- Listar objetos en un bucket:

```
aws --endpoint-url=http://localhost:4566 s3 ls s3://cloudnotes-bucket
``` 



## Errores
Durante el desarrollo de la aplicación, me encontré con varios errores relacionados con la configuración y uso de DynamoDB en LocalStack. A continuación, detallo los errores más comunes y sus soluciones:

1. **🔴ERROR: Nombre de la tabla en repository:** Al estar ejecutando localstack y hacer peticiones REST, tuve el siguiente error:
```
AWS dynamodb.PutItem => 400 (ResourceNotFoundException) ERROR 46399 --- [CloudNotes] [nio-8080-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet] : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException: Cannot do operations on a non-existent table (Service: DynamoDb, Status Code: 400, Request ID: 6a31130e-d971-489a-98a0-01d7be7ce68e) (SDK Attempt Count: 1)] with root cause
```

**🟢 SOLUCIÓN:** Esto fue slo cambiar el nombre de la tabla en el repositorio para que coincida con el nombre utilizado en la creación de la tabla en LocalStack.
ya que en la parte `client.table("Notess", TableSchema.fromBean(Note.class));`
Tenía una incoherencia en el nombre de la tabla, ya que en la creación de la tabla se llamaba "notes" y en el repositorio "Notess".

2. **🔴ERROR: Tabla mal creada en localStack**: Al corregir el error del nombre de la tabla, tube otro relacionado tambien con tablas.

```AWS dynamodb.PutItem => 400 (ValidationException) ERROR 47121 --- [CloudNotes] [nio-8080-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet] : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: software.amazon.awssdk.services.dynamodb.model.DynamoDbException: One of the required keys was not given a value (Service: DynamoDb, Status Code: 400, Request ID: c306ad33-6c1c-42ba-b4e0-315df4d0924f) (SDK Attempt Count: 1)] with root cause```

**🟢 SOLUCIÓN:** Este herror se debió a que en la creación de la tabla no se había definido correctamente la clave primaria. En DynamoDB, cada tabla debe tener una clave primaria definida para identificar de manera única cada ítem. En este caso, la tabla "notes" necesitaba una clave primaria llamada "id" de tipo String.

Mi tablero fue creado con `--attribute-definitions AttributeName=id,AttributeType=S
--key-schema AttributeName=id,KeyType=HASH` pero lo correcto era `--attribute-definitions AttributeName=noteId,AttributeType=S` ya que debe coincidir con el atributo anotado en la clase Note.

## Nuevos aprendizajes

-  private Instant createdAt: 'Instant' es una clase en Java que representa un punto específico en el tiempo, con precisión de nanosegundos. Se utiliza comúnmente para almacenar marcas de tiempo y realizar operaciones relacionadas con el tiempo.