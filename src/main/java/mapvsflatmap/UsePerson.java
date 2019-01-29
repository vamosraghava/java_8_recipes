package mapvsflatmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("Convert2MethodRef")
public class UsePerson {
    private static List<String> names =
            Arrays.asList("Grace Hopper", "Frances Allen", "Ada Lovelace",
                          "Barbara Liskov", "Adele Goldberg", "Karen Spärck Jones");

    public List<String> getNames() {
        return names;
    }

    public List<Person> createPersonListJava7() {
        List<Person> people = new ArrayList<>();
        for (String name : names) {
            people.add(new Person(name));
        }
        return people;
    }

    public List<Person> createPersonList() {
        return names.stream()                         // Stream<String>
                    .map(name -> new Person(name))    // Stream<Person>
                    .collect(Collectors.toList());    // List<Person>
    }

    public List<Person> createPersonList_CtorRef() {
        return names.stream()
                    .map(Person::new)  // invoke the one-arg Person ctor that takes String
                    .collect(Collectors.toList());
    }

    public List<Person> createPersonList_2ArgCtrRef() {
        return names.stream()                      // Stream<String>
                    .map(name -> name.split(" "))  // Stream<String[]>
                    .map(Person::new)              // Stream<Person> using String... ctor
                    .map(Person::new)              // Stream<Person> copies using the copy ctor
                    .collect(Collectors.toList());
    }

    public List<Person> createPersonLinkedList() {
        return names.stream()
                    .map(Person::new)
                    .collect(Collectors.toCollection(LinkedList::new));
    }

    public Person[] createPersonArray() {
        return names.stream()
                    .map(Person::new)
                    .toArray(Person[]::new);
    }


    // 1..5 | 6..10 | 11..15 | 16..20
    // add each element to LinkedList and return it (sequential)
    // Say we are using a parallel stream with four processes
    //  R1      R2      R3       R4
    //               R
    public List<Person> createPersonListUsingNew() {
        return names.stream()
                    // .parallel()
                    .map(Person::new)
                    .collect(LinkedList::new,
                             LinkedList::add,
                             LinkedList::addAll);
    }

    public List<Person> createPersonListUsingNewWithLambdas() {
        return names.stream()
                    .map(Person::new)
                    .collect(() -> new LinkedList<Person>(),
                             (people, e) -> people.add(e),
                             (totalCollection, people) ->
                                     totalCollection.addAll(people)); // not called unless parallel
    }

}
