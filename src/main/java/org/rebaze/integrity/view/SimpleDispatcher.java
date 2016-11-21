package org.rebaze.integrity.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@AllArgsConstructor
public class SimpleDispatcher<T> implements Dispatcher<T>
{
    private final TreeMapper mapper;

    @Getter
    private final Set<View<T>> views = new CopyOnWriteArraySet<>();

    public void register(View v) {
        views.add(v);
    }

    public void dispatch( T[] items) {
        // to be done.
        //Tree fastIndex = asTree( session,items );
        //View fastLane.get(fastIndex))

        for (View<T> v : views ) {
            ViewUpdate<T> updates = v.createUpdate();
            for (T p : items) {
                updates.add(mapper.mask( p ),p);
            }
            v.applyUpdate( updates );

        }
    }
}
