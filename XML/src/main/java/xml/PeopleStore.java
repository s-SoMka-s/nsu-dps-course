package xml;

import xml.consts.XmlTags;
import xml.models.Person;

import java.util.*;

public class PeopleStore {
    private final List<Person> people = new ArrayList<>();
    private final Map<String, Person> idMap = new HashMap<>();
    private final Map<String, Person> fullNameMap = new HashMap<>();
    private final Map<String, String> collisionIdToFullNameMap = new HashMap<>();
    private final Map<String, Set<String>> collisionFullNameToIdsMap = new HashMap<>();

    public Collection<Person> getPeople() {
        return idMap.values();
    }

    public void add(Person person) {
        people.add(person);
    }

    public int size() {
        return people.size();
    }

    public void ensureSuccess() {
        fillMaps();

        if (!mapValuesEqual()) {
            System.out.println("Persons were combined incorrectly!");
            return;
        }

        fullNameMap.clear();
        for (var person : idMap.values()) { // store every object in two maps
            fullNameMap.put(person.getFullName(), person);
        }

        for (var person : idMap.values()) {
            provideConsistency(person);
        }

        var allPersonsAreValid = true;
        for (var person : idMap.values()) {
            if (!person.isValid()) {
                System.out.println(person.getId() + " - INVALID PERSON");
                allPersonsAreValid = false;
            }
        }

        if (allPersonsAreValid) {
            System.out.println("ALL PERSONS ARE VALID!");
        }
    }

    private void fillMaps() {
        for (var person : people) {
            putInIdMap(person);

            putInFullNameMap(person);
        }

        for (var person : idMap.values()) {
            putInFullNameMap(person);
        }

        System.out.println(idMap.size() + " - idMap size");

        removeIdFullNameCollisions();

        for (var entry : collisionIdToFullNameMap.entrySet()) {
            var id = entry.getKey();
            var fullName = entry.getValue();

            var ids = collisionFullNameToIdsMap.get(fullName);
            if (ids == null) {
                collisionFullNameToIdsMap.put(fullName, new TreeSet<String>() {{
                    add(id);
                }});
            } else {
                ids.add(id);
            }
        }
    }

    private void putInIdMap(Person person) {
        var id = person.getId();
        if (id == null) {
            return;
        }

        if (!idMap.containsKey(person.getId())) {
            idMap.put(person.getId(), person);
        } else {
            idMap.get(person.getId()).combine(person);
        }
    }

    private void putInFullNameMap(Person person) {
        if (!person.isFullNameSet()) {
            return;
        }

        var fullName = person.getFullName();
        if (!fullNameMap.containsKey(fullName)) {
            fullNameMap.put(fullName, person);
        } else {
            fullNameMap.get(fullName).combine(person);

            var p = fullNameMap.get(fullName);
            if (person.getId() != null && p.getId() != null && !p.getId().equals(person.getId())) {
                collisionIdToFullNameMap.put(p.getId(), p.getFullName());
                collisionIdToFullNameMap.put(person.getId(), person.getFullName());
            }
        }
    }

    private boolean mapValuesEqual() {
        var people = idMap.values();

        return people.stream()
                .allMatch(p -> p.equals(fullNameMap.get(p.getFullName())));
    }

    private void removeIdFullNameCollisions(){
        for (var entry : collisionIdToFullNameMap.entrySet()) {
            idMap.remove(entry.getKey());
            fullNameMap.remove(entry.getValue());
        }
    }

    private void provideConsistency(Person person) {
        if (person.getGender().equals(XmlTags.MALE)) {
            trySetWife(person);
        }

        if (person.getGender().equals(XmlTags.FEMALE)) {
            trySetHusband(person);
        }

        trySetParents(person);

        trySetChildren(person);

        trySetSiblings(person);
    }

    private void trySetParents(Person person) {
        if (person.getFatherFullName() != null) {
            trySetFather(person);
        }

        if (person.getMotherFullName() != null) {
            trySetMother(person);
        }

        var parentIdIterator = person.getParentIdSet().iterator();
        while (parentIdIterator.hasNext()) {
            var parentId = parentIdIterator.next();
            var parent = idMap.get(parentId);
            if (parent != null) {
                if (parent.getGender().equals(XmlTags.MALE)) {
                    person.setFatherId(parent.getId());
                } else {
                    person.setMotherId(parent.getId());
                }
            } else {
                if (collisionIdToFullNameMap.containsKey(parentId)) {
                    parentIdIterator.remove();
                } else {
                    System.out.println(parentId + " - valid parentId is ___NOT___ found");
                }
            }
        }
    }

    private void trySetSiblings(Person person) {
        var siblingIdIterator = person.getSiblingIdSet().iterator();
        while (siblingIdIterator.hasNext()) {
            var siblingId = siblingIdIterator.next();
            var sibling = idMap.get(siblingId);
            if (sibling != null) {
                if (sibling.getGender().equals(XmlTags.MALE)) {
                    person.getBrotherIdSet().add(siblingId);
                } else {
                    person.getSisterIdSet().add(siblingId);
                }
            } else {
                if (collisionIdToFullNameMap.containsKey(siblingId)) {
                    siblingIdIterator.remove();
                    person.setSiblingRemoved(true);
                } else {
                    System.out.println(siblingId + " - valid siblingId is ___NOT___ found");
                }
            }
        }

        var brotherFullNameIterator = person.getBrotherFullNameSet().iterator();
        while (brotherFullNameIterator.hasNext()) {
            var brotherFullName = brotherFullNameIterator.next();
            var brother = fullNameMap.get(brotherFullName);
            if (brother != null) {
                person.getBrotherIdSet().add(brother.getId());
            } else {
                if (collisionFullNameToIdsMap.containsKey(brotherFullName)) {
                    brotherFullNameIterator.remove();
                    person.setSiblingRemoved(true);
                } else {
                    System.out.println(brotherFullName + " - valid brotherFullName is ___NOT___ found");
                }
            }
        }

        Iterator<String> sisterFullNameIterator = person.getSisterFullNameSet().iterator();
        while (sisterFullNameIterator.hasNext()) {
            String sisterFullName = sisterFullNameIterator.next();
            Person sister = fullNameMap.get(sisterFullName);
            if (sister != null) {
                person.getSisterIdSet().add(sister.getId());
            } else {
                if (collisionFullNameToIdsMap.containsKey(sisterFullName)) {
                    sisterFullNameIterator.remove();
                    person.setSiblingRemoved(true);
                } else {
                    System.out.println(sisterFullName + " - valid sisterFullName is ___NOT___ found");
                }
            }
        }
    }

    private void trySetChildren(Person person) {
        var sonIdIterator = person.getSonIdSet().iterator();
        while (sonIdIterator.hasNext()) {
            var sonId = sonIdIterator.next();
            if (!idMap.containsKey(sonId)) {
                if (collisionIdToFullNameMap.containsKey(sonId)) {
                    sonIdIterator.remove();
                    person.setChildRemoved(true);
                } else {
                    System.out.println(sonId + " - valid sonId is ___NOT___ found");
                }
            }
        }

        var daughterIdIterator = person.getDaughterIdSet().iterator();
        while (daughterIdIterator.hasNext()) {
            var daughterId = daughterIdIterator.next();
            if (!idMap.containsKey(daughterId)) {
                if (collisionIdToFullNameMap.containsKey(daughterId)) {
                    daughterIdIterator.remove();
                    person.setChildRemoved(true);
                } else {
                    System.out.println(daughterId + " - valid daughterId is ___NOT___ found");
                }
            }
        }

        var childFullNameIterator = person.getChildFullNameSet().iterator();
        while (childFullNameIterator.hasNext()) {
            var childFullName = childFullNameIterator.next();
            var child = fullNameMap.get(childFullName);
            if (child != null) {
                if (child.getGender().equals(XmlTags.MALE)) {
                    person.getSonIdSet().add(child.getId());
                } else {
                    person.getDaughterIdSet().add(child.getId());
                }
            } else {
                if (collisionFullNameToIdsMap.containsKey(childFullName)) {
                    childFullNameIterator.remove();
                    person.setChildRemoved(true);
                } else {
                    System.out.println(childFullName + " - valid childFullName is ___NOT___ found");
                }
            }
        }
    }

    private void trySetMother(Person person) {
        var mother = fullNameMap.get(person.getMotherFullName());
        if (mother != null) {
            person.setMotherId(mother.getId());
        } else {
            if (collisionFullNameToIdsMap.containsKey(person.getMotherFullName())) {
                person.setMotherFullName(null);
            } else {
                System.out.println(person.getMotherFullName() + " - valid MotherFullName is ___NOT___ found");
            }
        }
    }

    private void trySetFather(Person person) {
        var father = fullNameMap.get(person.getFatherFullName());
        if (father != null) {
            person.setFatherId(father.getId());
        } else {
            if (collisionFullNameToIdsMap.containsKey(person.getFatherFullName())) {
                person.setFatherFullName(null);
            } else {
                System.out.println(person.getFatherFullName() + " - valid FatherFullName is ___NOT___ found");
            }
        }
    }

    private void trySetWifeById(Person person) {
        var wife = idMap.get(person.getWifeId());
        if (wife != null) {
            person.setSpouseFullName(wife.getFullName());
        } else {
            if (collisionIdToFullNameMap.containsKey(person.getWifeId())) {
                person.setWifeId(null);
            } else {
                System.out.println(person.getWifeId() + " - valid WifeId is ___NOT___ found");
            }
        }
    }

    private void trySetWifeByFullName(Person person) {
        var wife = fullNameMap.get(person.getSpouseFullName());
        if (wife != null) {
            person.setWifeId(wife.getId());
        } else {
            if (collisionFullNameToIdsMap.containsKey(person.getSpouseFullName())) {
                person.setSpouseFullName(null);
            } else {
                System.out.println(person.getSpouseFullName() + " - valid SpouseFullName is ___NOT___ found");
            }
        }
    }

    private void trySetHusbandById(Person person) {
        var husband = idMap.get(person.getHusbandId());
        if (husband != null) {
            person.setSpouseFullName(husband.getFullName());
        } else {
            if (collisionIdToFullNameMap.containsKey(person.getHusbandId())) {
                person.setHusbandId(null);
            } else {
                System.out.println(person.getHusbandId() + " - valid HusbandId is ___NOT___ found");
            }
        }
    }

    private void trySetHusbandByFullName(Person person) {
        var husband = fullNameMap.get(person.getSpouseFullName());
        if (husband != null) {
            person.setHusbandId(husband.getId());
        } else {
            if (collisionFullNameToIdsMap.containsKey(person.getSpouseFullName())) {
                person.setSpouseFullName(null);
            } else {
                System.out.println(person.getSpouseFullName() + " - valid SpouseFullName is ___NOT___ found");
            }
        }
    }

    private void trySetHusband(Person person) {
        if (person.getHusbandId() != null && person.getSpouseFullName() == null) {
            trySetHusbandById(person);
        }

        if (person.getSpouseFullName() != null && person.getHusbandId() == null) {
            trySetHusbandByFullName(person);
        }
    }

    private void trySetWife(Person person) {
        if (person.getWifeId() != null && person.getSpouseFullName() == null) {
            trySetWifeById(person);
        }

        if (person.getSpouseFullName() != null && person.getWifeId() == null) {
            trySetWifeByFullName(person);
        }
    }
}
