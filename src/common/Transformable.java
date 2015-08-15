package common;

/**
 * Interface for objects that can be transformed with a PositionTransformer.
 */
public interface Transformable<T extends Transformable> {

  T transform(PositionTransformer transformer);
}
