export class Vector2{
    
    static up = Object.freeze(new Vector2(0, -1));
    static right = Object.freeze(new Vector2(1, 0));

    
    constructor(x, y){
        this.x = x;
        this.y = y;
    }


    add(v){
        return new Vector2(this.x + v.x, this.y+v.y);
    }

    sub(v){
        let inverted = v.scale(-1);
        return this.add(inverted);
    }


    scale(factor){
        return new Vector2(this.x * factor, this.y * factor);
    }

    equals(v){
        return (this.x === v.x) && (this.y === v.y);
    }

    round_components(){
        return new Vector2(Math.floor(this.x), Math.floor(this.y));
    }

    toString(){
        return `x=${this.x},y=${this.y}`;
    }
}