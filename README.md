# Cloud Notes

## Modulo cloud
Este módulo se encarga de la gestión de notas en la nube, permitiendo a los usuarios crear, leer, actualizar y eliminar notas almacenadas en un servicio en la nube.

Con fines eductivos y de no cometer herrores usando AWS directamenté, se usará LocalStack para simular los servicios de AWS en un entorno local.

### Errores en mi proceso

1. **Nombre de la tabla en repository:** Al estar ejecutando localstack y hacer peticiones REST, tuve el siguiente error:
```
AWS dynamodb.PutItem => 400 (ResourceNotFoundException) ERROR 46399 --- [CloudNotes] [nio-8080-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet] : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException: Cannot do operations on a non-existent table (Service: DynamoDb, Status Code: 400, Request ID: 6a31130e-d971-489a-98a0-01d7be7ce68e) (SDK Attempt Count: 1)] with root cause
```

2. **Tabla mal creada en localStack**: Al corregir el error del nombre de la tabla, tube otro relacionado tambien con tablas.

```AWS dynamodb.PutItem => 400 (ValidationException) ERROR 47121 --- [CloudNotes] [nio-8080-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet] : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: software.amazon.awssdk.services.dynamodb.model.DynamoDbException: One of the required keys was not given a value (Service: DynamoDb, Status Code: 400, Request ID: c306ad33-6c1c-42ba-b4e0-315df4d0924f) (SDK Attempt Count: 1)] with root cause```

### Soluciones de errores

1. **Nombre de la tabla en repository:** Esto fue slo cambiar el nombre de la tabla en el repositorio para que coincida con el nombre utilizado en la creación de la tabla en LocalStack.
   ya que en la parte `client.table("Notess", TableSchema.fromBean(Note.class));`

    Tenía una incoherencia en el nombre de la tabla, ya que en la creación de la tabla se llamaba "notes" y en el repositorio "Notess".


2. **Tabla mal creada en localStack**: Este herror se debió a que en la creación de la tabla no se había definido correctamente la clave primaria. En DynamoDB, cada tabla debe tener una clave primaria definida para identificar de manera única cada ítem. En este caso, la tabla "notes" necesitaba una clave primaria llamada "id" de tipo String.

    Mi tablero fue creado con `--attribute-definitions AttributeName=id,AttributeType=S
--key-schema AttributeName=id,KeyType=HASH` pero lo correcto era `--attribute-definitions AttributeName=noteId,AttributeType=S` ya que debe coincidir con el atributo anotado en la clase Note.


## Comandos

- Arrancar LocalStack:

```
localstack start
```

- Crear tabla en LocalStack:

```
aws dynamodb create-table \
  --endpoint-url http://localhost:4566 \
  --table-name notes \
  --attribute-definitions AttributeName=noteId,AttributeType=S \
  --key-schema AttributeName=noteId,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST
```

- Verificar tablas creadas en LocalStack:

```
aws dynamodb list-tables --endpoint-url http://localhost:4566
```

- Validar datos en tablas

```
aws dynamodb scan --table-name notes --endpoint-url http://localhost:4566
```

```
aws dynamodb scan \
  --table-name notes \
  --endpoint-url http://localhost:4566
```

- crear tabla NoteAttachments

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

## Nuevos aprendizajes

-  private Instant createdAt: 'Instant' es una clase en Java que representa un punto específico en el tiempo, con precisión de nanosegundos. Se utiliza comúnmente para almacenar marcas de tiempo y realizar operaciones relacionadas con el tiempo.