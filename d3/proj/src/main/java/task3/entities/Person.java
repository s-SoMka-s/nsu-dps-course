package task3.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {
    Integer id;
    String name;
    Gender gender;
    Integer fatherId;
    Integer motherId;
    Integer spouseId;

    public Person() {
        clear();
    }

    public void clear() {
        id = null;
        name = null;
        gender = null;
        fatherId = null;
        motherId = null;
        spouseId = null;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", fatherId=" + fatherId +
                ", motherId=" + motherId +
                ", spouseId=" + spouseId +
                '}';
    }
}
