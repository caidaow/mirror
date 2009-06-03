/**
 * 
 */
package net.vidageek.mirror.reflect;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import net.vidageek.mirror.fixtures.BeanFixture;
import net.vidageek.mirror.fixtures.ChildFixture;
import net.vidageek.mirror.fixtures.ChildHidingFixture;
import net.vidageek.mirror.fixtures.ConstructorFixture;
import net.vidageek.mirror.fixtures.FieldFixture;
import net.vidageek.mirror.fixtures.MethodFixture;
import net.vidageek.mirror.fixtures.NotABeanFixture;
import net.vidageek.mirror.provider.ReflectionProvider;
import net.vidageek.mirror.provider.java.PureJavaReflectionProvider;

import org.junit.Before;
import org.junit.Test;

/**
 * @author jonasabreu
 * 
 */
public class AllReflectionHandlerTest {

    private ReflectionProvider provider;

    @Before
    public void setup() {
        provider = new PureJavaReflectionProvider();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatThrowsExceptionIfClassIsNull() {
        new DefaultAllReflectionHandler<Object>(provider, null);
    }

    @Test
    public void testThatReflectAllFields() {
        List<Field> fields = new DefaultAllReflectionHandler<FieldFixture>(provider, FieldFixture.class).fields();

        assertEquals(8, fields.size());
    }

    @Test
    public void testThatReflectHierarchyFields() {
        List<Field> fields = new DefaultAllReflectionHandler<ChildFixture>(provider, ChildFixture.class).fields();

        assertEquals(5, fields.size());
    }

    @Test
    public void testThatFindsHidingFields() {
        List<Field> fields = new DefaultAllReflectionHandler<ChildHidingFixture>(provider, ChildHidingFixture.class)
                                                                                                                    .fields();

        assertEquals(2, fields.size());
    }

    @Test
    public void testThatReflectAllMethods() {
        List<Method> methods = new DefaultAllReflectionHandler<MethodFixture>(provider, MethodFixture.class).methods();

        assertEquals(22, methods.size());
    }

    @Test
    public void testThatReflectHierarchyMethods() {
        List<Method> methods = new DefaultAllReflectionHandler<ChildFixture>(provider, ChildFixture.class).methods();

        assertEquals(16, methods.size());
    }

    @Test
    public void testThatFindsOverridingMethods() {
        List<Method> methods = new DefaultAllReflectionHandler<ChildHidingFixture>(provider, ChildHidingFixture.class)
                                                                                                                      .methods();

        assertEquals(14, methods.size());
    }

    @Test
    public void testThatReflectsAllConstructors() {
        List<Constructor<ConstructorFixture>> constructors = new DefaultAllReflectionHandler<ConstructorFixture>(
                provider, ConstructorFixture.class).constructors();

        assertEquals(6, constructors.size());
    }

    @Test
    public void testThatReflectsAllSetters() {
        List<Method> setters = new DefaultAllReflectionHandler<BeanFixture>(provider, BeanFixture.class).setters();
        assertEquals(1, setters.size());
        assertEquals("setField", setters.get(0).getName());
    }

    @Test
    public void testThatDoesntReflectMethodAsSetterIfArgumentNumberIsNotOneOrReturnTypeIsNotVoid() {
        List<Method> setters = new DefaultAllReflectionHandler<NotABeanFixture>(provider, NotABeanFixture.class)
                                                                                                                .setters();
        assertEquals(0, setters.size());
    }

    @Test
    public void testThatReflectsAllGetters() {
        List<Method> getters = new DefaultAllReflectionHandler<BeanFixture>(provider, BeanFixture.class).getters();
        assertEquals(2, getters.size());
        assertEquals("getField", getters.get(0).getName());
        assertEquals("getClass", getters.get(1).getName());
    }

    @Test
    public void testThatDoesntReflectMethodAsGetterIfArgumentNumberIsNotZeroOrReturnTypeIsVoid() {
        List<Method> getters = new DefaultAllReflectionHandler<NotABeanFixture>(provider, NotABeanFixture.class)
                                                                                                                .getters();
        assertEquals(1, getters.size());
        assertEquals("getClass", getters.get(0).getName());
    }
}