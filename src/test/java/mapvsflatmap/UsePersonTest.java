package mapvsflatmap;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

public class UsePersonTest {
    private final UsePerson up = new UsePerson();
    private Person[] people;

    @BeforeEach
    public void setUp() {
        people = up.getNames().stream()
                .map(Person::new)
                .toArray(Person[]::new);
    }

    @Test
    public void createPersonListJava7() {
        assertThat(up.createPersonListJava7(), containsInAnyOrder(people));
    }

    @Test
    public void createPersonList() {
        assertThat(up.createPersonList(), containsInAnyOrder(people));
    }

    @Test
    public void createPersonList_CtorRef() {
        assertThat(up.createPersonList_CtorRef(), containsInAnyOrder(people));
    }

    @Test
    public void createPersonList_2ArgCtrRef() {
        assertThat(up.createPersonList_2ArgCtrRef(), containsInAnyOrder(people));
    }

    @Test
    public void copyConstructor() {
        Person before = new Person("Grace Hopper");

        List<Person> people = Stream.of(before)
                .collect(Collectors.toList());
        Person after = people.get(0);

        assertSame(before, after);  // same object

        before.setName("Grace Murray Hopper");
        assertEquals("Grace Murray Hopper", after.getName());

        people = Stream.of(before)
                .map(Person::new)
                .collect(Collectors.toList());
        after = people.get(0);
        assertNotSame(before, after);  // different objects
        assertEquals(before, after);   // but equivalent

        before.setName("Rear Admiral Dr. Grace Murray Hopper");
        assertNotEquals(before, after);
    }

    @Test
    public void varargsConstructor() {
        List<Person> persons = Arrays.stream(people)
                .map(Person::getName)          // Stream<String>
                .map(name -> name.split(" "))  // Stream<String[]>
                .map(Person::new)              // Stream<Person> using String... ctor
                .collect(Collectors.toList());
        assertThat(persons, containsInAnyOrder(people));
    }

    @Test
    public void createPersonLinkedList() {
        assertThat(up.createPersonLinkedList(), containsInAnyOrder(people));
    }

    @Test
    public void createPersonArray() {
        assertThat(Arrays.asList(up.createPersonArray()), containsInAnyOrder(people));
    }

    @Test
    public void createPersonListUsingNew() {
        assertThat(up.createPersonListUsingNew(), containsInAnyOrder(people));
    }

    @Test
    public void createPersonListUsingNewWithLambdas() {
        assertThat(up.createPersonListUsingNewWithLambdas(), containsInAnyOrder(people));
    }
}