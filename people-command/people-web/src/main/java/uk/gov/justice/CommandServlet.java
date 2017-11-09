package uk.gov.justice;

import uk.gov.justice.schema.catalog.CatalogLoader;
import uk.gov.justice.schema.catalog.SchemaDictionary;

import java.io.IOException;

import javax.inject.Inject;
import javax.jws.soap.InitParam;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name="commandServlet",

        urlPatterns={"/command"}, loadOnStartup = 1

        )
public class CommandServlet extends javax.servlet.http.HttpServlet {

    @Inject
    SchemaDictionary dictionary;

    @Inject
    CatalogLoader catalogLoader;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("<html><body>Hello!</body></html>");
        resp.getWriter().flush();

        System.out.println(dictionary);


        //CatalogLoader catalogLoader = new CatalogLoader();
        catalogLoader.loadCatalogsFromClasspath();



        System.out.println("Printing dictionary number of entries: " + dictionary.size());
        for (String key : dictionary.getIDSet()
                ) {
            System.out.println(key);
        }
    }


    @Override
    public void init() throws ServletException {



    }
}
