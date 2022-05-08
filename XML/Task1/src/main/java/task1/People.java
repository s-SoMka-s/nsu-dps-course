package task1;

import java.util.ArrayList;
import task1.entities.Person;

public class People {
  private final ArrayList<Person> people;
  private final ArrayList<Person> unknownPeople;
  private final long count;

  public People(long count) {
    this.count = count;
    people = new ArrayList<>();
    unknownPeople = new ArrayList<>();
  }

  public Person firstOrNull(String idOrFullName) {
    if (isIdString(idOrFullName)) {
      return findById(idOrFullName);
    }

    return findByFullName(idOrFullName);

  }

  private Person findById(String id) {
    if (id == null || id.isBlank() || id.isEmpty()) {
      return null;
    }

    return this.people.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
  }

  private Person findByFullName(String fullName) {
    return this.people.stream().filter(p -> p.getFullName().toLowerCase().equals(fullName)).findFirst().orElse(null);
  }

  public void addPerson(Person person) {
    this.people.add(person);
  }

  public Person createUnknownPerson() {
    var person = new Person();
    return person;
  }

  private boolean isIdString(String raw) {
    if (raw.isBlank()) {
      return false;
    }

    return raw.matches("p\\d.");
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();

    for (var person : people) {
      builder.append("===========\n");
      builder.append(person.toString());
    }

    builder.append("===========\n");
    builder.append("count: " + count);
    builder.append("\n");

    return builder.toString();
  }
}
