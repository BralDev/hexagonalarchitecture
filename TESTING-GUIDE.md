# Guía de Testing en Arquitectura Hexagonal

## 📚 Estructura de Tests Creados

### 1️⃣ Tests de Dominio (Puros)
**Ubicación:** `src/test/java/.../domain/model/`

- ✅ **[DocumentTypeTest.java](src/test/java/com/example/hexagonalarchitecture/users/domain/model/DocumentTypeTest.java)** - Tests parametrizados para validación de formatos
- ✅ **[UserTest.java](src/test/java/com/example/hexagonalarchitecture/users/domain/model/UserTest.java)** - Tests del modelo de dominio (records)

**Características:**
- Sin frameworks ni mocks
- Tests unitarios puros
- Validación de lógica de negocio
- Muy rápidos de ejecutar

### 2️⃣ Tests de Casos de Uso (Application Layer)
**Ubicación:** `src/test/java/.../application/port/in/`

- ✅ **[CreateUserUseCaseTest.java](src/test/java/com/example/hexagonalarchitecture/users/application/port/in/CreateUserUseCaseTest.java)** - Tests con mocks del repositorio

**Características:**
- Usa **Mockito** para simular dependencias
- Testea toda la lógica del caso de uso
- Incluye casos exitosos y excepciones
- Verifica que la contraseña sea hasheada
- No requiere base de datos

**Casos testeados:**
- ✓ Creación exitosa
- ✓ Validación de formato de documento
- ✓ Username duplicado
- ✓ Email duplicado
- ✓ Documento duplicado
- ✓ Edad menor a 18 años
- ✓ Edad exactamente 18 años
- ✓ Password hasheado correctamente

### 3️⃣ Tests de Integración (Infrastructure)
**Ubicación:** `src/test/java/.../infraestructure/controller/`

- ✅ **[UserControllerIntegrationTest.java](src/test/java/com/example/hexagonalarchitecture/users/infraestructure/controller/UserControllerIntegrationTest.java)** - Tests end-to-end del API

**Características:**
- Usa `@SpringBootTest` con toda la aplicación
- Base de datos H2 en memoria
- MockMvc construido manualmente con `webAppContextSetup()`
- Tests HTTP completos (POST, GET, PUT, DELETE)
- Validación de respuestas JSON
- Transaccional para limpiar datos entre tests
- ObjectMapper creado localmente en `setUp()` con `findAndRegisterModules()`

**Endpoints testeados:**
- ✓ POST `/users` - Crear usuario
- ✓ GET `/users/{id}` - Obtener usuario
- ✓ PUT `/users/{id}` - Actualizar usuario
- ✓ DELETE `/users/{id}` - Eliminar usuario (soft-delete, retorna 200)
- ✓ GET `/users` - Buscar usuarios con filtros

### 4️⃣ Configuración de Tests
- ✅ **[application-test.yaml](src/test/resources/application-test.yaml)** - Base de datos H2 para tests (sin variables de entorno)
- ✅ **[pom.xml](pom.xml)** - Dependencias H2 y spring-boot-test-autoconfigure agregadas
- ✅ **@ActiveProfiles("test")** - En tests para usar configuración de test

---

## 🚀 Cómo Ejecutar los Tests

### Todos los tests:
```bash
./mvnw test
```

### Solo tests de una clase:
```bash
./mvnw test -Dtest=CreateUserUseCaseTest
```

### Solo un test específico:
```bash
./mvnw test -Dtest=CreateUserUseCaseTest#testCreateValidUser
```

### Con reporte de cobertura:
```bash
./mvnw test jacoco:report
```

---

## 📊 Pirámide de Testing

```
       /\
      /  \  E2E (Integration Tests)
     /____\
    /      \
   / Unit+  \ UseCase Tests (with Mocks)
  /   Use    \
 /____________\
/              \
/  Unit Tests   \ Domain Model Tests
/________________\
```

**Distribución recomendada:**
- 70% - Tests unitarios (dominio + casos de uso)
- 20% - Tests de integración
- 10% - Tests E2E

---

## 🎯 Mejores Prácticas Aplicadas

### 0. **Setup de MockMvc para Tests de Integración**
```java
@SpringBootTest
@ActiveProfiles("test")  // Usa H2 en lugar de PostgreSQL
@Transactional           // Auto-rollback después de cada test
class UserControllerIntegrationTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @BeforeEach
    void setUp() {
        // MockMvc construido manualmente (sin @AutoConfigureMockMvc)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // ObjectMapper creado localmente con módulos registrados
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
        userRepository.deleteAll();
    }
}
```

### 1. **Rutas Correctas en Tests de Integración**
```java
// ✅ CORRECTO - rutas sin /api
mockMvc.perform(post("/users")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(userRequest)))
    .andExpect(status().isCreated())

// ❌ INCORRECTO - la ruta /api/users no existe
mockMvc.perform(post("/api/users")...)
```

### 2. **PageResponse - Estructura Correcta**
```java
// PageResponse tiene estructura: { "data": [...], "meta": {...} }
mockMvc.perform(get("/users?lastName=Doe"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
    .andExpect(jsonPath("$.data[0].lastName").value("Doe"))
    .andExpect(jsonPath("$.meta.page").exists())

// ❌ INCORRECTO - no existe $.content
.andExpect(jsonPath("$.content[0].lastName").value("Doe"))
```

### 3. **Soft-Delete Behavior**
```java
// DELETE retorna 204 No Content
mockMvc.perform(delete("/users/" + savedUser.getId()))
    .andExpect(status().isNoContent());

// Luego GET retorna 200 OK con status = DELETED (soft delete)
mockMvc.perform(get("/users/" + savedUser.getId()))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.status").value("DELETED"));

// Usuario NO se busca en resultados con status ACTIVE (hay que filtrar)
```

### 4. **UpdateUserRequest - Campos Obligatorios**
```java
// UpdateUserRequest requiere: username, firstName, lastName, status
Map<String, Object> updateRequest = new HashMap<>();
updateRequest.put("username", "jdoe");           // OBLIGATORIO
updateRequest.put("firstName", "John");
updateRequest.put("lastName", "Doe");
updateRequest.put("status", "ACTIVE");           // OBLIGATORIO
updateRequest.put("email", "john@example.com");
updateRequest.put("phone", "123456789");
updateRequest.put("address", "New Address");
updateRequest.put("birthDate", "1990-01-01");

mockMvc.perform(put("/users/" + id)
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(updateRequest)))
    .andExpect(status().isOk());
```

### 5. **AAA Pattern** (Arrange-Act-Assert)
```java
@Test
void testCreateValidUser() {
    // Given - Preparar datos
    User inputUser = new User(...);
    
    // When - Ejecutar acción
    User result = createUserUseCase.execute(inputUser, "password");
    
    // Then - Verificar resultado
    assertNotNull(result);
}
```

### 6. **Tests Descriptivos**
```java
@DisplayName("Lanzar excepción si el usuario es menor de 18 años")
void testCreateUserUnder18YearsOld() { ... }
```

### 7. **Tests Parametrizados** (Múltiples casos)
```java
@ParameterizedTest
@CsvSource({
    "DNI, 12345678, true",
    "DNI, 123, false"
})
void testDniValidation(DocumentType type, String number, boolean expected) {
    assertEquals(expected, type.isValidNumber(number));
}
```

### 8. **Mocks Inteligentes**
```java
@Mock
private UserRepositoryPort userRepository;

when(userRepository.existsByUsername("jdoe")).thenReturn(true);
verify(userRepository, never()).create(any(), any());
```

### 9. **Tests Transaccionales**
```java
@Transactional  // Auto-rollback después de cada test
@BeforeEach
void setUp() {
    userRepository.deleteAll();  // DB limpia
}
```

---

## 🛠️ Tecnologías Usadas

- **JUnit 5** - Framework de testing
- **Mockito** - Mocks y stubs
- **AssertJ** (incluido en Spring Boot Test) - Assertions fluidas
- **Spring Boot Test** - Tests de integración
- **MockMvc** - Tests de controladores REST
- **H2 Database** - Base de datos en memoria para tests

---

## 📝 Próximos Pasos

### Para expandir los tests, puedes crear:

1. **Más tests de casos de uso:**
   - `UpdateUserUseCaseTest`
   - `DeleteUserUseCaseTest`
   - `SearchUsersUseCaseTest`
   - `ChangePasswordUseCaseTest`

2. **Tests de adaptadores:**
   - `JpaUserRepositoryAdapterTest` (con `@DataJpaTest`)
   - `UserSpecificationsTest`

3. **Tests de validaciones:**
   - Tests del `GlobalExceptionHandler`
   - Tests de DTOs con `@Valid`

4. **Cobertura de código:**
   Agregar JaCoCo al `pom.xml`:
   ```xml
   <plugin>
       <groupId>org.jacoco</groupId>
       <artifactId>jacoco-maven-plugin</artifactId>
       <version>0.8.11</version>
       <executions>
           <execution>
               <goals>
                   <goal>prepare-agent</goal>
               </goals>
           </execution>
           <execution>
               <id>report</id>
               <phase>test</phase>
               <goals>
                   <goal>report</goal>
               </goals>
           </execution>
       </executions>
   </plugin>
   ```

---

## 💡 Ventajas de Testing en Hexagonal Architecture

✅ **Aislamiento:** Cada capa se testea independientemente  
✅ **Mocks fáciles:** Los puertos son interfaces, fáciles de mockear  
✅ **Rapidez:** Tests unitarios sin frameworks son ultra-rápidos  
✅ **Confianza:** Cubres todas las capas desde dominio hasta API  
✅ **Refactoring seguro:** Los tests te avisan si rompes algo  

## ⚙️ Configuración Importante para Tests

### Perfil @ActiveProfiles("test")
**Esencial para que los tests usen H2 y no intenten conectar a PostgreSQL:**

```yaml
# src/test/resources/application-test.yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  jpa:
    hibernate:
      ddl-auto: create-drop  # Recrea tablas automáticamente
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.H2Dialect
  h2:
    console:
      enabled: true

logging:
  level:
    com.example.hexagonalarchitecture: DEBUG
    org.hibernate.SQL: DEBUG
```

### Cómo usarlo en tests:
```java
@SpringBootTest
@ActiveProfiles("test")    // Carga application-test.yaml
@Transactional            // Rollback automático
class UserControllerIntegrationTest {
  ...
}
```

### Dependencias Necesarias en pom.xml
```xml
<!-- Spring Boot Test (incluye JUnit 5, Mockito, AssertJ) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- AutoConfiguration de Spring Test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-test-autoconfigure</artifactId>
    <scope>test</scope>
</dependency>

<!-- JSON/Jackson para ObjectMapper -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-json</artifactId>
</dependency>

<!-- Base de datos H2 para tests -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```  

---

## 🎓 Conceptos Clave

- **Test Unitario:** Testea una unidad (clase/método) de forma aislada
- **Test de Integración:** Testea múltiples componentes juntos
- **Mock:** Objeto falso que simula comportamiento de dependencias
- **Stub:** Respuesta predefinida a una llamada
- **Spy:** Mock parcial, llama al objeto real pero puede ser mockeado

---

**¡Ahora ejecuta `./mvnw test` y verás todos los tests pasar en verde! ✅**
