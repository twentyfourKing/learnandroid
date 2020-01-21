package follow.twentyfourking.greendao_use_annotation;

import org.greenrobot.greendao.converter.PropertyConverter;

import follow.twentyfourking.greendao_use_annotation.dao.NoteType;

public class NoteTypeConverter implements PropertyConverter<NoteType, String> {
    @Override
    public NoteType convertToEntityProperty(String databaseValue) {
        return NoteType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(NoteType entityProperty) {
        return entityProperty.name();
    }
}
