package ru.ipim.phonebook.model;

import lombok.*;
import org.springframework.core.style.ToStringCreator;

//Response Model
@Data
@AllArgsConstructor
@Getter
@Setter
public class StatAddress {
    private String address;
    private Long   cnt;

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("address", this.getAddress())
                .append("cnt", this.getCnt().toString())
                .toString();
    }
}
