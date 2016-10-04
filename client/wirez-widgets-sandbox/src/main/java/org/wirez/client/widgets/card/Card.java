package org.wirez.client.widgets.card;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.Command;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: unused for now - improve performance by only processing and adding into the DOM tree the current card side.
@Dependent
public class Card implements IsWidget {

    private static Logger LOGGER = Logger.getLogger(Card.class.getName());
    
    public interface View extends UberView<Card> {

        View setFlipCallback( Command flipCallback );
        
        View setFront( IsWidget frontWidget );

        View setBack( IsWidget backtWidget );
        
        View clearFront();

        View clearBack();

        View add( IsWidget widget );
        
        View flip();
        
    }

    View view;
    private Command flipCallback = null;
    private boolean isFront = true;
    private IsWidget front = null;
    private IsWidget back = null;
    private Timer clearTimer = null;
    
    @Inject
    public Card(final View view) {
        this.view = view;
    }

    @PostConstruct
    public void init() {
        view.init(this);
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }
   
    public void setFlipCallback( final Command flipCallback ) {
        this.flipCallback = flipCallback;
    }

    private final Command c = () -> {

        Card.this.isFront = !isFront();

        showView();

        if ( null != flipCallback ) {
            flipCallback.execute();
        }

    };
    
    public void show( final IsWidget front, 
                      final IsWidget back ) {
        
        this.front = front;
        this.back = back;
        
        view.setFlipCallback( c );
        
        showView();
        
    }
    
    private void showView() {
        
        final Command[] clearCommand = new Command[1];
        if ( isFront ) {
            clearCommand[0] = () ->  view.clearBack();;
            view.setFront( front );
        } else {
            clearCommand[0] = () ->  view.clearFront();;
            view.setBack( back );
        }

        if ( null != clearTimer && clearTimer.isRunning() ) {
            clearTimer.cancel();
        }
        
        clearTimer = new Timer() {
            @Override
            public void run() {
                clearCommand[0].execute();
            }
        };

        clearTimer.schedule(2000);
        
    }
    
    public void flip() {
        view.flip();
    }
    
    public void add( IsWidget widget ) {
        view.add( widget );
    }

    public boolean isFront() {
        return isFront;
    }

    private void log(final Level level, final String message) {
        if ( LogConfiguration.loggingIsEnabled() ) {
            LOGGER.log(level, message);
        }
    }

}
