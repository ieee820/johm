package redis.clients.johm;

import org.junit.Test;

import redis.clients.johm.models.Country;
import redis.clients.johm.models.User;

public class ModelTest extends JOhmTestBase {

    @Test(expected = MissingIdException.class)
    public void cannotGetIdWhenNew() {
	User user = new User();
	user.getId();
    }

    @Test
    public void checkModelPersistence() {
	User user = new User();
	user.setName("foo");
	user.setRoom("vroom");
	user = user.save();

	assertNotNull(user);
	assertEquals(1, user.getId());
	User savedUser = JOhm.get(User.class, user.getId());
	assertEquals(user.getName(), savedUser.getName());
	assertNull(savedUser.getRoom());
	assertEquals(user.getId(), savedUser.getId());
	assertEquals(user.getAge(), savedUser.getAge());
    }

    @Test
    public void checkModelPersistenceOtherValueTypes() {
	User user1 = new User();
	user1.setName("foo");
	user1.setRoom("vroom");
	user1.setAge(99);
	user1.setSalary(9999.99f);
	user1.setInitial('f');
	user1 = user1.save();

	User user2 = new User();
	user2.setName("foo2");
	user2.setRoom("vroom2");
	user2.setAge(9);
	user2.setInitial('f');
	user2 = user2.save();

	User user3 = new User();
	user3.setName("foo3");
	user3.setRoom("vroom3");
	user3.setAge(19);
	user3.setSalary(9999.9f);
	user3.setInitial('f');
	user3 = user3.save();

	assertNotNull(user1);
	assertEquals(1, user1.getId());
	User savedUser1 = JOhm.get(User.class, user1.getId());
	assertEquals(user1.getName(), savedUser1.getName());
	assertNull(savedUser1.getRoom());
	assertEquals(user1.getId(), savedUser1.getId());
	assertEquals(user1.getAge(), savedUser1.getAge());
	assertEquals(user1.getInitial(), savedUser1.getInitial());
	assertEquals(user1.getSalary(), savedUser1.getSalary(), 0D);

	assertNotNull(user2);
	assertEquals(2, user2.getId());
	User savedUser2 = JOhm.get(User.class, user2.getId());
	assertEquals(user2.getName(), savedUser2.getName());
	assertNull(savedUser2.getRoom());
	assertEquals(user2.getId(), savedUser2.getId());
	assertEquals(user2.getInitial(), savedUser2.getInitial());
	assertEquals(user2.getAge(), savedUser2.getAge());

	assertNotNull(user3);
	assertEquals(3, user3.getId());
	User savedUser3 = JOhm.get(User.class, user3.getId());
	assertEquals(user3.getName(), savedUser3.getName());
	assertNull(savedUser3.getRoom());
	assertEquals(user3.getId(), savedUser3.getId());
	assertEquals(user3.getAge(), savedUser3.getAge());
	assertEquals(user3.getInitial(), savedUser3.getInitial());
	assertEquals(user3.getSalary(), savedUser3.getSalary(), 0D);
    }

    @Test
    public void checkModelDeletion() {
	User user = new User();
	user.save();
	int id = user.getId();

	assertNotNull(JOhm.get(User.class, id));
	assertTrue(user.delete());
	assertNull(JOhm.get(User.class, id));

	user = new User();
	user.save();
	id = user.getId();

	assertNotNull(JOhm.get(User.class, id));
	assertTrue(JOhm.delete(User.class, id));
	assertNull(JOhm.get(User.class, id));
    }

    @Test
    public void shouldNotPersistFieldsWithoutAttributeAnnotation() {
	User user = new User();
	user.setName("foo");
	user.setRoom("3A");
	user.save();

	User savedUser = JOhm.get(User.class, user.getId());
	assertEquals(user.getName(), savedUser.getName());
	assertNull(savedUser.getRoom());
    }

    @Test(expected = MissingIdException.class)
    public void shouldFailWhenReferenceWasNotSaved() {
	User user = new User();
	user.setName("bar");
	user.setCountry(new Country());
	user.save();
    }

    @Test
    public void shouldHandleReferences() {
	User user = new User();
	user.setName("foo");
	user.setRoom("3A");
	user.save();

	User savedUser = JOhm.get(User.class, user.getId());
	assertNull(savedUser.getCountry());

	Country somewhere = new Country();
	somewhere.setName("Somewhere");
	somewhere.save();

	user = new User();
	user.setName("bar");
	user.setCountry(somewhere);
	user.save();

	savedUser = JOhm.get(User.class, user.getId());
	assertNotNull(savedUser.getCountry());
	assertEquals(somewhere.getId(), savedUser.getCountry().getId());
	assertEquals(somewhere.getName(), savedUser.getCountry().getName());
    }
}