import uk.gov.justice.schema.catalog.CatalogLoader;

public class PeopleCommand {

    public static void main(String[] args)
    {
        CatalogLoader catalogLoader = new CatalogLoader();
        catalogLoader.loadCatalogsFromClasspath();



    }
}
