package crawler.dao;

import java.util.List;

/**
 * Created by watson zhang on 16/9/20.
 */
public interface BasicBox<TEM> {

    public int getBoxSize(TEM cache);

    public TEM getOneFromBox(TEM cache);

    public List<TEM> getListFromBox(TEM cache, int start, int end);

    public List<TEM> getListFromBox(TEM cache, int num);

    public boolean addOne(TEM cache);

    public int addList(List<TEM> cache);

}
