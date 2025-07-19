package ru.aston.intensive.notificationservice.services;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.aston.intensive.notificationservice.dto.EmailNotificationDto;
import ru.aston.intensive.notificationservice.model.EmailNotificationEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-19T12:28:11+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (JetBrains s.r.o.)"
)
@Component
public class EmailMapperImpl implements EmailMapper {

    @Override
    public EmailNotificationDto toEmailDto(EmailNotificationEntity emailEntity) {
        if ( emailEntity == null ) {
            return null;
        }

        EmailNotificationDto emailNotificationDto = new EmailNotificationDto();

        emailNotificationDto.setEmail( emailEntity.getEmail() );
        emailNotificationDto.setStatus( emailEntity.getStatus() );
        emailNotificationDto.setEventType( emailEntity.getEventType() );
        emailNotificationDto.setTimeStamp( emailEntity.getTimeStamp() );

        return emailNotificationDto;
    }
}
