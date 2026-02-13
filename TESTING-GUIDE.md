# Guía de Testing en Arquitectura Hexagonal

## 📚 Estructura de Tests Creados

He creado ejemplos completos de los **4 niveles de testing** para tu proyecto:

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
- Tests HTTP completos (POST, GET, PUT, DELETE)
- Validación de respuestas JSON
- Transaccional para limpiar datos

**Endpoints testeados:**
- ✓ POST `/api/users` - Crear usuario
- ✓ GET `/api/users/{id}` - Obtener usuario
- ✓ PUT `/api/users/{id}` - Actualizar usuario
- ✓ DELETE `/api/users/{id}` - Eliminar usuario
- ✓ GET `/api/users/search` - Buscar usuarios

### 4️⃣ Configuración de Tests
- ✅ **[application-test.yaml](src/test/resources/application-test.yaml)** - Base de datos H2 para tests
- ✅ **[pom.xml](pom.xml)** - Dependencia H2 agregada

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

### 1. **AAA Pattern** (Arrange-Act-Assert)
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

### 2. **Tests Descriptivos**
```java
@DisplayName("Lanzar excepción si el usuario es menor de 18 años")
void testCreateUserUnder18YearsOld() { ... }
```

### 3. **Tests Parametrizados** (Múltiples casos)
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

### 4. **Mocks Inteligentes**
```java
@Mock
private UserRepositoryPort userRepository;

when(userRepository.existsByUsername("jdoe")).thenReturn(true);
verify(userRepository, never()).create(any(), any());
```

### 5. **Tests Transaccionales**
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

---

## 🎓 Conceptos Clave

- **Test Unitario:** Testea una unidad (clase/método) de forma aislada
- **Test de Integración:** Testea múltiples componentes juntos
- **Mock:** Objeto falso que simula comportamiento de dependencias
- **Stub:** Respuesta predefinida a una llamada
- **Spy:** Mock parcial, llama al objeto real pero puede ser mockeado

---

**¡Ahora ejecuta `./mvnw test` y verás todos los tests pasar en verde! ✅**
