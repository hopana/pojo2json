package testdata.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericTestPOJO {

    private List listNonGeneric = new ArrayList();

    private Map mapNonGeneric = new HashMap();


    private List<Integer> list = new ArrayList<>();

    private List<Integer[]> listArr = new ArrayList<>();

    private List<List<Integer>[]> listListArr = new ArrayList<>();

    private List<Type> listEnum = new ArrayList<>();

    private List<List<Integer>> listList = new ArrayList<>();

    private List<List<List<Integer>>> listListList = new ArrayList<>();

    private List<Object> listObject = new ArrayList<>();

    private List<?> listGenericObject = new ArrayList<>();

    // --

    private Generic<Integer> generic = new Generic<>();

    private Generic<Integer[]> genericArr = new Generic<>();

    private Generic<List<Integer>[]> genericListArr = new Generic<>();

    private Generic<Type> genericEnum = new Generic<>();

    private Generic<Generic<Integer>> genericGeneric = new Generic<>();

    private Generic<Generic<Generic<Integer>>> genericGenericGeneric = new Generic<>();

    private Generic<Object> genericObject = new Generic<>();

    private Generic<?> genericGenericObject = new Generic<>();

    public class Generic<T> {

        private String username;

        private T data;
    }

    public enum Type {
        TYPE_A,
        TYPE_B,
        TYPE_C;
    }

    private Generics nonGenerics = new Generics();

    private Generics<String, Integer, List> generics = new Generics<>();

    public class Generics<A, B, C> {
        private A a;
        private B b;
        private C c;
    }
}
