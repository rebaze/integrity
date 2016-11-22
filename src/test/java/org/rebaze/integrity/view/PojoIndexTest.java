package org.rebaze.integrity.view;

import lombok.Data;
import org.junit.Test;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeBuilder;
import org.rebaze.integrity.tree.api.TreeSession;
import org.rebaze.integrity.tree.util.DefaultTreeSessionFactory;
import org.rebaze.integrity.tree.util.TreeConsoleFormatter;
import org.rebaze.integrity.view.api.Dispatcher;
import org.rebaze.integrity.view.api.TreeMapper;
import org.rebaze.integrity.view.api.View;
import org.rebaze.integrity.view.util.SimpleDispatcher;
import org.rebaze.integrity.view.util.TreeBasedView;
import org.rebaze.integrity.view.util.TreeViewDefinition;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertTrue;

/**
 * A sample usecase
 */
public class PojoIndexTest
{

    public static final TreeConsoleFormatter PRINT = new TreeConsoleFormatter();

    public static final TreeSession session = new DefaultTreeSessionFactory().create();

    class ItemTreeMapper implements TreeMapper<Item>
    {
        public  Tree mask(  Item p1 )
        {
            TreeBuilder tb = session.createTreeBuilder();
            tb.branch( "lastName" ).add( p1.lastName );
            tb.branch( "firstName" ).add( p1.firstName );
            return tb.seal();
        }

        @Override public TreeSession getSession()
        {
            return session;
        }
    }

    @Data
    public class Item
    {

        private final String lastName;

        private final String firstName;

        private final int age;

        private final double points;

        private final boolean isFemale;

    }

    @Data
    public class Group
    {
        private final Item[] items;
    }

    @Test public void testDumbDispatcher()
    {
        TreeMapper<Item> mapper = new ItemTreeMapper();

        View<Item> view1 = new TreeBasedView<>( "Duck Family", new TreeViewDefinition<>( new Item[] {
            new Item( "Duck", "Donald", 20, 42d, false ),
            new Item( "Duck", "Tick", 20, 3d, true ),
            new Item( "Duck", "Track", 20, 3d, true )
        }, mapper ) );

        View<Item> view2 = new TreeBasedView<>("Maus Family", new TreeViewDefinition<>( new Item[] {
            new Item( "Maus", "Mickey", 20, 42d, false ),
            new Item( "Maus", "Mini", 20, 3d, true ),
        }, mapper ) );

        Dispatcher<Item> dispatcher = new SimpleDispatcher<>(mapper);
        // register views:
        dispatcher.register( view1 );
        dispatcher.register( view2 );


        // Time this:
        for (Group g : populateSampleData())
        {
            dispatcher.dispatch(g.getItems());
        }

        // END.

        assertTrue(view1 + " received updates", view1.getUpdates() > 0);
        assertTrue(view2 + " received updates", view2.getUpdates() > 0);
        printView( view1 );
        printView( view2 );


        //PRINT.prettyPrint( p1Tree,p2Tree );
    }

    private Group[] populateSampleData()
    {
        Group[] seed = new Group[] {
            new Group( new Item[] {
                new Item( "None", "Donald", 20, 42d, false ),
                new Item( "Foo", "Tick", 20, 3d, true ),
                new Item( "Other", "Track", 20, 3d, true )
            } ),
            new Group( new Item[] {
                new Item( "Bacon", "Donald", 20, 42d, false ),
                new Item( "Chees", "Tick", 20, 3d, true ),
                new Item( "Marmelade", "Track", 20, 3d, true )
            } ),
            new Group( new Item[] {
                new Item( "Duck", "Donald", 20, 42d, false ),
                new Item( "Foo", "Tick", 20, 3d, true ),
                new Item( "Other", "Track", 20, 3d, true )
            } ),
            new Group( new Item[] {
                new Item( "None", "Donald", 20, 42d, false ),
                new Item( "Foo", "Tick", 20, 3d, true ),
                new Item( "Other", "Track", 20, 3d, true ),
                new Item( "Hannover", "Donald", 20, 42d, false ),
                new Item( "Duck", "Tick", 20, 3d, true ),
                new Item( "Maus", "Mini", 20, 3d, true )
            } ),

        };

        return populateVariation(1000,seed);
    }

    private void printView( View<Item> v )
    {
        System.out.println("View " + v + " got " + v.getUpdates() + " updates.");

        for (Item item : v.getState(new Item[v.getDefinition().size()])) {
            System.out.println("Value: " + item);
        }
    }

    private Group[] populateVariation( int population, Group[] seedGroup )
    {
        Group[] group = new Group[population];
        int counter = 0;
        while (counter < population)
        {
            for ( Group g : seedGroup )
            {
                if (counter < population) {
                    Item[] people = new Item[g.getItems().length];
                    for (int i = 0;i<people.length;i++) {
                        people[i] = g.getItems()[i];
                        double points = ThreadLocalRandom.current().nextDouble(0,10);
                        people[i] = new Item( people[i].lastName, people[i].firstName, people[i].age, points, people[i].isFemale );
                    }
                    group[counter] = new Group(people);
                    counter ++;
                }
            }
        }
        return group;
    }


}
