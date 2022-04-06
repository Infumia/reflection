package tr.com.infumia.reflection.constructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tr.com.infumia.reflection.RefConstructed;

/**
 * an implementation for {@link RefConstructed}.
 *
 * @param <T> the result instance's type.
 */
@Log
@RequiredArgsConstructor
public final class ConstructorOf<T> implements RefConstructed<T> {

  /**
   * the constructor.
   */
  @NotNull
  private final Constructor<T> constructor;

  @NotNull
  @Override
  public Optional<T> create(@Nullable final Object... parameters) {
    final var accessible = this.constructor.isAccessible();
    try {
      this.constructor.setAccessible(true);
      return Optional.of(this.constructor.newInstance(parameters));
    } catch (final IllegalAccessException | InstantiationException | InvocationTargetException exception) {
      ConstructorOf.log.log(Level.SEVERE, "ConstructorOf#create(Object[])", exception);
      return Optional.empty();
    } finally {
      this.constructor.setAccessible(accessible);
    }
  }

  @NotNull
  @Override
  public <A extends Annotation> Optional<A> getAnnotation(@NotNull final Class<A> annotationClass) {
    return Optional.ofNullable(this.constructor.getDeclaredAnnotation(annotationClass));
  }

  @NotNull
  @Override
  public <A extends Annotation> Collection<A> getAnnotations(@NotNull final Class<A> annotationClass) {
    return List.of(this.constructor.getDeclaredAnnotationsByType(annotationClass));
  }

  @Override
  public boolean hasFinal() {
    return Modifier.isFinal(this.constructor.getModifiers());
  }

  @Override
  public boolean hasPrivate() {
    return Modifier.isPrivate(this.constructor.getModifiers());
  }

  @Override
  public boolean hasPublic() {
    return Modifier.isPublic(this.constructor.getModifiers());
  }

  @Override
  public boolean hasStatic() {
    return Modifier.isStatic(this.constructor.getModifiers());
  }
}
