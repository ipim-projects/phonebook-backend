package ru.ipim.phonebook.model;

import lombok.*;
import org.springframework.core.style.ToStringCreator;

//Response Model
@Data
@AllArgsConstructor
@Getter
@Setter
public class StatCompany {
        private String company;
        private Long   cnt;

        @Override
        public String toString() {
                return new ToStringCreator(this)
                        .append("company", this.getCompany())
                        .append("cnt", this.getCnt().toString())
                        .toString();
        }
}
