package crawler;

/**
 * Created by watson zhang on 16/9/19.
 */
public interface LinkFilter {
    public boolean accept(String url);
}
