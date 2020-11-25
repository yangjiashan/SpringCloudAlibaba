public class FclassTest<E> {

    private E outPut;

    public E getOutPut() {
        return outPut;
    }

    public void setOutPut(E outPut) {
        this.outPut = outPut;
    }

    public static void main(String[] args) {

        FclassTest<String> fclassTest = new FclassTest();

        fclassTest.setOutPut("1");

    }


}
