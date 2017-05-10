/**
 * Created by silasa on 3/22/17.
 */
public class Item {
    int ammo;
    int clipAmmo;
    ItemStatus itemStatus;
    int progressCounter;
    int typeID;

    void first(){
        if (itemStatus == ItemStatus.IDLE){
            itemStatus = ItemStatus.FIRE;
        }
    }
    void second(){

    }
    void third(){

    }

    void addAmmo(int bulletCount){
        ammo+=bulletCount;
    }

    public Item(int typeID, int ammo, int clipAmmo){
        this.typeID = typeID;
        this.ammo = ammo;
        this.clipAmmo = clipAmmo;
    }
}
