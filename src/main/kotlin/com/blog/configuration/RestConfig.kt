package com.blog.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class RestConfig(val loggingInterceptor: LogInterceptor) : WebMvcConfigurer {


    @Bean
    fun securityFilter(http : HttpSecurity): SecurityFilterChain {
        http.csrf{csrf -> csrf.disable()}
            .authorizeHttpRequests{auth -> auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers(HttpMethod.GET,"v1/api/blogs/**").permitAll()
            .anyRequest().hasAuthority("ROLE_ADMIN")
        }.httpBasic(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info( Info().title("Blog Api").version("1.0"))
            .components( Components()
                .addSecuritySchemes("basicScheme",
                     SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
                        .addSecurityItem( SecurityRequirement().addList("basicScheme"))
    }
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loggingInterceptor)
    }

}