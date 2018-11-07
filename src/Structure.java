public abstract class Structure {
    private int ownerIndex;

    public Structure(int ownerIndex){
        this.ownerIndex = ownerIndex;
    }

    public int getOwnerIndex() {
        return ownerIndex;
    }
}
