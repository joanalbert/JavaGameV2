'use strict';

/* ================================================================
   VECTOR2
================================================================ */
class Vector2 {
    constructor(x = 0, y = 0) { this.x = x; this.y = y; }
    static zero()              { return new Vector2(0, 0); }
    static one()               { return new Vector2(1, 1); }
    static from(v)             { return new Vector2(v.x, v.y); }
    static fromArray([x, y])   { return new Vector2(x, y); }
    add(v)     { return new Vector2(this.x + v.x, this.y + v.y); }
    sub(v)     { return new Vector2(this.x - v.x, this.y - v.y); }
    mul(v)     { return new Vector2(this.x * v.x, this.y * v.y); }
    div(v)     { return new Vector2(this.x / v.x, this.y / v.y); }
    scale(s)   { return new Vector2(this.x * s, this.y * s); }
    negate()   { return new Vector2(-this.x, -this.y); }
    addScalar(s) { return new Vector2(this.x + s, this.y + s); }
    subScalar(s) { return new Vector2(this.x - s, this.y - s); }
    length()        { return Math.sqrt(this.x * this.x + this.y * this.y); }
    lengthSq()      { return this.x * this.x + this.y * this.y; }
    normalize()     { const l = this.length(); return l ? this.scale(1/l) : Vector2.zero(); }
    dot(v)          { return this.x * v.x + this.y * v.y; }
    cross(v)        { return this.x * v.y - this.y * v.x; }
    distanceTo(v)   { return this.sub(v).length(); }
    distanceSqTo(v) { return this.sub(v).lengthSq(); }
    angle()         { return Math.atan2(this.y, this.x); }
    angleTo(v)      { return Math.atan2(v.y - this.y, v.x - this.x); }
    rotate(rad)     { const c = Math.cos(rad), s = Math.sin(rad); return new Vector2(this.x*c - this.y*s, this.x*s + this.y*c); }
    lerp(v, t)      { return new Vector2(this.x + (v.x-this.x)*t, this.y + (v.y-this.y)*t); }
    floor()  { return new Vector2(Math.floor(this.x), Math.floor(this.y)); }
    ceil()   { return new Vector2(Math.ceil(this.x),  Math.ceil(this.y)); }
    round()  { return new Vector2(Math.round(this.x), Math.round(this.y)); }
    abs()    { return new Vector2(Math.abs(this.x),   Math.abs(this.y)); }
    min(v)   { return new Vector2(Math.min(this.x, v.x), Math.min(this.y, v.y)); }
    max(v)   { return new Vector2(Math.max(this.x, v.x), Math.max(this.y, v.y)); }
    clamp(mn, mx) { return this.max(mn).min(mx); }
    equals(v)     { return this.x === v.x && this.y === v.y; }
    equalsApprox(v, eps = 1e-6) { return Math.abs(this.x-v.x) < eps && Math.abs(this.y-v.y) < eps; }
    toArray()  { return [this.x, this.y]; }
    toObject() { return { x: this.x, y: this.y }; }
    toString() { return `(${this.x}, ${this.y})`; }
    clone()    { return new Vector2(this.x, this.y); }
    set(x, y)     { this.x = x; this.y = y; return this; }
    setX(x)       { this.x = x; return this; }
    setY(y)       { this.y = y; return this; }
    iaddScalar(s) { this.x += s; this.y += s; return this; }
}


/* ================================================================
   TILE DEFINITION
================================================================ */
class TileDefinition {
    constructor({ id, atlasId, atlasOffset, atlasSize, label, category, subcategory }) {
        this.id          = id;
        this.atlasId     = atlasId;
        this.atlasOffset = atlasOffset instanceof Vector2 ? atlasOffset : new Vector2(atlasOffset.x, atlasOffset.y);
        this.atlasSize   = atlasSize   instanceof Vector2 ? atlasSize   : new Vector2(atlasSize.x,   atlasSize.y);
        this.label       = label;
        this.category    = category;
        this.subcategory = subcategory;
    }
}


/* ================================================================
   TILE INSTANCE
================================================================ */
class Tile {
    constructor(def, tilePos, layerIndex) {
        this.id          = def.id;
        this.defId       = def.id;
        this.atlasOffset = def.atlasOffset.clone();
        this.atlasSize   = def.atlasSize.clone();
        this.atlasId     = def.atlasId;
        this.tilePos     = tilePos.clone();
        this.layerIndex  = layerIndex;
        this.collision   = 'NONE';
    }
    screenPos(tileSize) { return this.tilePos.scale(tileSize); }
    get mapKey() { return `${this.tilePos.x},${this.tilePos.y}`; }
    toJSON() {
        return {
            layer_index:  this.layerIndex,
            tile_pos:     this.tilePos.toObject(),
            atlas_id:     this.atlasId,
            atlas_pos: this.atlasOffset.toObject(),
            collision:   this.collision,
        };
        
        /*return {
            defId:       this.defId,
            tilePos:     this.tilePos.toObject(),
            layerIndex:  this.layerIndex,
            atlasOffset: this.atlasOffset.toObject(),
            atlasSize:   this.atlasSize.toObject(),
            atlasId:     this.atlasId,
            collision:   this.collision,
        };*/
    }
}


/* ================================================================
   TILE STAMP
================================================================ */
class TileStamp {
    constructor(atlasId, startOffsetPx, widthTiles, heightTiles) {
        this.atlasId     = atlasId;
        this.startOffset = startOffsetPx.clone();
        this.width       = widthTiles;
        this.height      = heightTiles;
        this.tileSize    = new Vector2(32, 32);
    }

    getTiles(baseTilePos, layerIndex, catalog) {
        const tiles = [];
        const ts    = this.tileSize.x;
        for (let dy = 0; dy < this.height; dy++) {
            for (let dx = 0; dx < this.width; dx++) {
                const offset  = this.startOffset.add(new Vector2(dx * ts, dy * ts));
                const def     = this._getOrCreateDef(this.atlasId, offset, catalog);
                const tilePos = baseTilePos.add(new Vector2(dx, dy));
                tiles.push(new Tile(def, tilePos, layerIndex));
            }
        }
        return tiles;
    }

    _getOrCreateDef(atlasId, offset, catalog) {
        for (const def of catalog.defs.values()) {
            if (def.atlasId === atlasId && def.atlasOffset.equals(offset)) return def;
        }
        const id  = `dynamic_${atlasId}_${offset.x}_${offset.y}`;
        const def = new TileDefinition({
            id, atlasId,
            atlasOffset: offset.clone(),
            atlasSize:   this.tileSize.clone(),
            label:       `Tile ${Math.floor(offset.x/32)},${Math.floor(offset.y/32)}`,
            category:    atlasId,
            subcategory: 'stamp',
        });
        catalog.defs.set(id, def);
        return def;
    }
}


/* ================================================================
   TILE ATLAS
================================================================ */
class TileAtlas {
    constructor(id, src, tileSize = new Vector2(32, 32)) {
        this.id       = id;
        this.tileSize = tileSize;
        this.loaded   = false;
        this.image    = new Image();
        this.image.onload = () => { this.loaded = true; };
        this.image.src = src;
    }
}


/* ================================================================
   MAP LAYER
================================================================ */
class MapLayer {
    constructor(index, name = `Layer ${index + 1}`) {
        this.index   = index;
        this.name    = name;
        this.visible = true;
        this.tiles   = new Map();
    }
    setTile(tile)   { this.tiles.set(tile.mapKey, tile); }
    removeTile(pos) { this.tiles.delete(`${pos.x},${pos.y}`); }
    getTile(pos)    { return this.tiles.get(`${pos.x},${pos.y}`) || null; }
    hasTile(pos)    { return this.tiles.has(`${pos.x},${pos.y}`); }
    clear()         { this.tiles.clear(); }
    toJSON() {
        return {
            index:   this.index,
            name:    this.name,
            visible: this.visible,
            tiles:   [...this.tiles.values()].map(t => t.toJSON()),
        };
    }
}


/* ================================================================
   TILE MAP
================================================================ */
class TileMap {
    constructor(name = 'Untitled Map', size = new Vector2(20, 15), tileSize = 32) {
        this.name             = name;
        this.size             = size.clone();
        this.tileSize         = tileSize;
        this.layers           = [new MapLayer(0), new MapLayer(1), new MapLayer(2)];
        this.activeLayerIndex = 0;
    }
    get activeLayer() { return this.layers[this.activeLayerIndex]; }
    get pixelSize()   { return this.size.scale(this.tileSize); }
    isInBounds(pos)   { return pos.x >= 0 && pos.y >= 0 && pos.x < this.size.x && pos.y < this.size.y; }
    resize(newSize, newTileSize) {
        this.size     = newSize.clone();
        this.tileSize = newTileSize;
        for (const layer of this.layers) {
            for (const [key, tile] of layer.tiles) {
                if (!this.isInBounds(tile.tilePos)) layer.tiles.delete(key);
            }
        }
    }
    addLayer() {
        const idx   = this.layers.length;
        const layer = new MapLayer(idx, `Layer ${idx + 1}`);
        this.layers.push(layer);
        return layer;
    }
    topTileAt(pos) {
        for (let i = this.layers.length - 1; i >= 0; i--) {
            const tile = this.layers[i].getTile(pos);
            if (tile && this.layers[i].visible) return tile;
        }
        return null;
    }
    toJSON() {
        return { name: this.name, size: this.size.toObject(), tileSize: this.tileSize, layers: this.layers.map(l => l.toJSON()) };
    }
}


/* ================================================================
   HISTORY MANAGER
================================================================ */
class Command { execute() {} undo() {} }

class PaintCommand extends Command {
    constructor(layer, newTile, oldTile) {
        super();
        this.layer   = layer;
        this.newTile = newTile;
        this.oldTile = oldTile;
        this.pos     = (newTile || oldTile).tilePos.clone();
    }
    execute() { if (this.newTile) this.layer.setTile(this.newTile); else this.layer.removeTile(this.pos); }
    undo()    { if (this.oldTile) this.layer.setTile(this.oldTile); else this.layer.removeTile(this.pos); }
}

class BatchCommand extends Command {
    constructor(commands) { super(); this.commands = commands; }
    execute() { this.commands.forEach(c => c.execute()); }
    undo()    { [...this.commands].reverse().forEach(c => c.undo()); }
}

class HistoryManager {
    constructor(maxSteps = 80) { this.maxSteps = maxSteps; this.undoStack = []; this.redoStack = []; }
    push(cmd)  { cmd.execute(); this.undoStack.push(cmd); if (this.undoStack.length > this.maxSteps) this.undoStack.shift(); this.redoStack = []; }
    undo()     { const cmd = this.undoStack.pop(); if (!cmd) return false; cmd.undo();    this.redoStack.push(cmd); return true; }
    redo()     { const cmd = this.redoStack.pop(); if (!cmd) return false; cmd.execute(); this.undoStack.push(cmd); return true; }
    clear()    { this.undoStack = []; this.redoStack = []; }
}


/* ================================================================
   SELECTION MANAGER
================================================================ */
class SelectionManager {
    constructor() {
        this.active      = false;
        this.start       = Vector2.zero();
        this.end         = Vector2.zero();
        this.isMoving    = false;
        this._moveAnchor = Vector2.zero();
        this._moveCache  = null;
    }
    begin(pos)  { this.active = true; this.start = pos.clone(); this.end = pos.clone(); }
    update(pos) { this.end = pos.clone(); }
    finish()    { const mn = this.start.min(this.end), mx = this.start.max(this.end); this.start = mn; this.end = mx; }
    clear()     { this.active = false; this.isMoving = false; this._moveCache = null; }
    get rect()  { const mn = this.start.min(this.end), mx = this.start.max(this.end); return { x: mn.x, y: mn.y, w: mx.x - mn.x + 1, h: mx.y - mn.y + 1 }; }
    contains(pos) { const r = this.rect; return pos.x >= r.x && pos.x < r.x + r.w && pos.y >= r.y && pos.y < r.y + r.h; }

    startMove(clickPos, layer) {
        if (!this.active) return;
        const r = this.rect;
        this._moveAnchor = new Vector2(clickPos.x - r.x, clickPos.y - r.y);
        this.isMoving    = true;
        const tiles = [];
        for (let y = r.y; y < r.y + r.h; y++)
            for (let x = r.x; x < r.x + r.w; x++) {
                const tile = layer.getTile(new Vector2(x, y));
                if (tile) tiles.push(tile);
            }
        this._moveCache = { layer, tiles, oldOrigin: new Vector2(r.x, r.y) };
    }

    updateMove(cursorPos) {
        if (!this.isMoving) return;
        const r = this.rect, w = r.w, h = r.h;
        const newStart = new Vector2(cursorPos.x - this._moveAnchor.x, cursorPos.y - this._moveAnchor.y);
        this.start = newStart.clone();
        this.end   = new Vector2(newStart.x + w - 1, newStart.y + h - 1);
    }

    finishMove() { this.isMoving = false; }
}


/* ================================================================
   RENDERER
================================================================ */
// Collision overlay colours — used in both draw and collision modes
const COLLISION_COLORS = Object.freeze({
    WALK:  'rgba(76,175,80,0.45)',
    BLOCK: 'rgba(244,67,54,0.45)',
    SURF:  'rgba(33,150,243,0.45)',
});

class Renderer {
    constructor(canvas) {
        this.canvas = canvas;
        this.ctx    = canvas.getContext('2d');
        this.ctx.imageSmoothingEnabled = false;
    }

    resize(pixelSize) {
        this.canvas.width  = pixelSize.x;
        this.canvas.height = pixelSize.y;
        this.ctx.imageSmoothingEnabled = false;
    }

    render(map, atlases, showCollision, selection, hoverPos, activeStamp, activeTool, hoveredActionIndex = -1, movePreview = null, activeCollisionType = 'NONE', activeCollisionTool = 'brush') {
        const { ctx } = this;
        const ts = map.tileSize;
        const sw = map.size.x * ts;
        const sh = map.size.y * ts;

        ctx.clearRect(0, 0, sw, sh);
        ctx.fillStyle = '#0d0f12';
        ctx.fillRect(0, 0, sw, sh);

        // Always draw checkerboard placeholders
        for (let y = 0; y < map.size.y; y++)
            for (let x = 0; x < map.size.x; x++)
                this._drawPlaceholder(new Vector2(x, y), ts);

        // Draw tiles — always draw the actual tile graphic regardless of mode
        for (const layer of map.layers) {
            if (!layer.visible) continue;
            for (const tile of layer.tiles.values()) {
                if (movePreview && movePreview.tileKeys.has(`${tile.tilePos.x},${tile.tilePos.y}`) && layer === movePreview.layer) continue;
                this._drawTile(tile, atlases, ts);
            }
        }

        // In collision mode, draw the collision overlay ON TOP of tiles
        if (showCollision) {
            for (const layer of map.layers) {
                if (!layer.visible) continue;
                for (const tile of layer.tiles.values()) {
                    if (tile.collision !== 'NONE') {
                        this._drawCollisionOverlay(tile, ts);
                    }
                }
            }
        }

        this._drawGrid(map, showCollision);

        if (movePreview) this._drawMovePreview(movePreview, atlases, ts, showCollision);

        if (selection?.active) {
            this._drawSelection(selection, ts);
            this._drawSelectionActions(selection, ts, hoveredActionIndex);
        }

        // Ghost / hover logic
        const stampIsMulti  = activeStamp && (activeStamp.width > 1 || activeStamp.height > 1);
        const NO_GHOST_TOOLS = new Set(['select', 'eraser', 'eyedropper']);
        const suppressGhost  = NO_GHOST_TOOLS.has(activeTool) || (activeTool === 'fill' && stampIsMulti);

        if (hoverPos && hoveredActionIndex === -1) {
            // In collision mode, show a coloured hover cell instead of the stamp ghost
            if (showCollision) {
                this._drawCollisionHover(hoverPos, ts, activeCollisionType, activeCollisionTool);
            } else {
                this._drawHover(hoverPos, ts);
                if (!suppressGhost && activeStamp) {
                    this._drawStampPreview(hoverPos, activeStamp, atlases, ts);
                }
            }
        }
    }

    // ---- tile drawing ----

    _drawTile(tile, atlases, ts) {
        const atlas = atlases.get(tile.atlasId);
        if (!atlas || !atlas.loaded) { this._drawPlaceholder(tile.tilePos, ts); return; }
        const sp = tile.tilePos.scale(ts);
        this.ctx.drawImage(atlas.image, tile.atlasOffset.x, tile.atlasOffset.y, tile.atlasSize.x, tile.atlasSize.y, sp.x, sp.y, ts, ts);
    }

    // Collision overlay — drawn transparently over the tile graphic
    _drawCollisionOverlay(tile, ts) {
        const sp  = tile.tilePos.scale(ts);
        const col = COLLISION_COLORS[tile.collision];
        if (!col) return;
        this.ctx.fillStyle = col;
        this.ctx.fillRect(sp.x, sp.y, ts, ts);

        // Small label letter
        let size = Math.max(8, ts * 0.28);
        this.ctx.fillStyle    = 'rgba(255,255,255,0.9)';
        this.ctx.font         = "bold"+size+"px Share Tech Mono, monospace";
        this.ctx.textAlign    = 'center';
        this.ctx.textBaseline = 'middle';
        this.ctx.fillText(tile.collision[0], sp.x + ts / 2, sp.y + ts / 2);
    }

    // Hover preview cell when in collision paint mode
    _drawCollisionHover(tilePos, ts, collisionType, collisionTool) {
        const sp  = tilePos.scale(ts);
        const ctx = this.ctx;

        if (collisionTool === 'eraser') {
            ctx.strokeStyle = 'rgba(220,50,50,0.9)';
            ctx.lineWidth   = 1.5;
            ctx.strokeRect(sp.x + 0.5, sp.y + 0.5, ts - 1, ts - 1);
            ctx.strokeStyle = 'rgba(220,50,50,0.7)';
            ctx.lineWidth   = 1;
            ctx.beginPath(); ctx.moveTo(sp.x + 4, sp.y + 4); ctx.lineTo(sp.x + ts - 4, sp.y + ts - 4); ctx.stroke();
            ctx.beginPath(); ctx.moveTo(sp.x + ts - 4, sp.y + 4); ctx.lineTo(sp.x + 4, sp.y + ts - 4); ctx.stroke();

        } else if (collisionTool === 'fill') {
            const col = COLLISION_COLORS[collisionType];
            if (col) {
                ctx.fillStyle = col.replace('0.45', '0.35');
                ctx.fillRect(sp.x, sp.y, ts, ts);
            }
            ctx.strokeStyle = col || 'rgba(220,50,50,0.85)';
            ctx.lineWidth   = 1.5;
            ctx.setLineDash([3, 2]);
            ctx.strokeRect(sp.x + 0.5, sp.y + 0.5, ts - 1, ts - 1);
            ctx.setLineDash([]);
            ctx.fillStyle    = 'rgba(255,255,255,0.85)';
            ctx.font         = `${Math.max(8, Math.round(ts * 0.3))}px system-ui`;
            ctx.textAlign    = 'right';
            ctx.textBaseline = 'bottom';
            ctx.fillText('⬡', sp.x + ts - 2, sp.y + ts - 1);
            ctx.textAlign    = 'center';
            ctx.textBaseline = 'middle';

        } else {
            const col = COLLISION_COLORS[collisionType];
            if (col) {
                ctx.fillStyle = col.replace('0.45', '0.3');
                ctx.fillRect(sp.x, sp.y, ts, ts);
            }
            ctx.strokeStyle = col || 'rgba(220,50,50,0.85)';
            ctx.lineWidth   = 1.5;
            ctx.strokeRect(sp.x + 0.5, sp.y + 0.5, ts - 1, ts - 1);
        }
    }

    _drawPlaceholder(tilePos, ts) {
        const sp   = tilePos.scale(ts);
        const ctx  = this.ctx;
        const sq   = Math.max(4, Math.floor(ts / 8));
        const cols = Math.ceil(ts / sq);
        const rows = Math.ceil(ts / sq);
        const LIGHT = '#3a3a3a', DARK = '#252525';
        for (let row = 0; row < rows; row++)
            for (let col = 0; col < cols; col++) {
                ctx.fillStyle = (row + col) % 2 === 0 ? LIGHT : DARK;
                ctx.fillRect(sp.x + col * sq, sp.y + row * sq, Math.min(sq, ts - col * sq), Math.min(sq, ts - row * sq));
            }
    }

    _drawGrid(map, showCollision) {
        const { ctx } = this;
        const ts = map.tileSize, sw = map.size.x * ts, sh = map.size.y * ts;
        ctx.strokeStyle = showCollision ? 'rgba(80,100,130,0.5)' : 'rgba(26,34,53,0.9)';
        ctx.lineWidth   = 0.5;
        for (let x = 0; x <= map.size.x; x++) { ctx.beginPath(); ctx.moveTo(x*ts,0); ctx.lineTo(x*ts,sh); ctx.stroke(); }
        for (let y = 0; y <= map.size.y; y++) { ctx.beginPath(); ctx.moveTo(0,y*ts); ctx.lineTo(sw,y*ts); ctx.stroke(); }
        ctx.strokeStyle = '#8b000088'; ctx.lineWidth = 1.5;
        ctx.strokeRect(0.75, 0.75, sw - 1.5, sh - 1.5);
    }

    _drawSelection(selection, ts) {
        const r = selection.rect, ctx = this.ctx;
        ctx.fillStyle = 'rgba(232,160,64,0.08)'; ctx.strokeStyle = '#e8a040'; ctx.lineWidth = 1.5;
        ctx.setLineDash([4, 3]);
        ctx.fillRect(r.x*ts, r.y*ts, r.w*ts, r.h*ts);
        ctx.strokeRect(r.x*ts+0.5, r.y*ts+0.5, r.w*ts-1, r.h*ts-1);
        ctx.setLineDash([]);
    }

    _drawHover(tilePos, ts) {
        const sp = tilePos.scale(ts), ctx = this.ctx;
        ctx.strokeStyle = 'rgba(220, 50, 50, 0.85)'; ctx.lineWidth = 1;
        ctx.strokeRect(sp.x + 0.5, sp.y + 0.5, ts - 1, ts - 1);
    }

    _drawStampPreview(hoverPos, stamp, atlases, ts) {
        const atlas = atlases.get(stamp.atlasId);
        if (!atlas || !atlas.loaded) return;
        const base = hoverPos.scale(ts);
        for (let dy = 0; dy < stamp.height; dy++)
            for (let dx = 0; dx < stamp.width; dx++) {
                const offset = stamp.startOffset.add(new Vector2(dx*ts, dy*ts));
                const sp     = base.add(new Vector2(dx*ts, dy*ts));
                this.ctx.globalAlpha = 0.55;
                this.ctx.drawImage(atlas.image, offset.x, offset.y, ts, ts, sp.x, sp.y, ts, ts);
                this.ctx.globalAlpha = 1;
            }
    }

    _drawMovePreview(movePreview, atlases, ts, showCollision) {
        const { tiles, delta } = movePreview;
        this.ctx.globalAlpha = 0.75;
        for (const tile of tiles) {
            const newPos = tile.tilePos.add(delta);
            const sp     = newPos.scale(ts);
            const atlas  = atlases.get(tile.atlasId);
            if (atlas?.loaded) {
                this.ctx.drawImage(atlas.image, tile.atlasOffset.x, tile.atlasOffset.y, tile.atlasSize.x, tile.atlasSize.y, sp.x, sp.y, ts, ts);
            }
            if (showCollision && tile.collision !== 'NONE') {
                this.ctx.globalAlpha = 0.45;
                this.ctx.fillStyle = COLLISION_COLORS[tile.collision] || '';
                this.ctx.fillRect(sp.x, sp.y, ts, ts);
                this.ctx.globalAlpha = 0.75;
            }
        }
        this.ctx.globalAlpha = 1;
    }

    _drawSelectionActions(selection, ts, hoveredActionIndex = -1) {
        if (!selection?.active) return;
        const bar = Renderer.getActionBarRect(selection, ts);
        if (!bar) return;
        const ctx      = this.ctx;
        const ICONS    = ['✋', '↻', '↺', '🗑', '✕'];
        const COLORS   = ['#60a5fa', '#a3e635', '#a3e635', '#f87171', '#94a3b8'];
        const HOVER_BG = ['rgba(96,165,250,0.2)','rgba(163,230,53,0.2)','rgba(163,230,53,0.2)','rgba(248,113,113,0.2)','rgba(148,163,184,0.2)'];
        ctx.fillStyle = 'rgba(13,17,30,0.96)'; ctx.strokeStyle = '#e8a040'; ctx.lineWidth = 1.5;
        ctx.beginPath(); ctx.roundRect(bar.x, bar.y, bar.w, bar.h, 5); ctx.fill(); ctx.stroke();
        ctx.font = `${Math.round(bar.iconW * 0.6)}px system-ui`; ctx.textAlign = 'center'; ctx.textBaseline = 'middle';
        for (let i = 0; i < ICONS.length; i++) {
            const slotX = bar.x + bar.pad + i * bar.iconW, slotY = bar.y + bar.pad;
            const cx = slotX + bar.iconW / 2, cy = slotY + bar.iconW / 2;
            if (i === hoveredActionIndex) {
                ctx.fillStyle = HOVER_BG[i];
                ctx.beginPath(); ctx.roundRect(slotX+2, slotY+2, bar.iconW-4, bar.iconW-4, 4); ctx.fill();
            }
            ctx.fillStyle = i === hoveredActionIndex ? COLORS[i] : COLORS[i] + '99';
            ctx.fillText(ICONS[i], cx, cy);
        }
    }

    static getActionBarRect(selection, tileSize) {
        if (!selection?.active) return null;
        const r = selection.rect, ICON = 30, PAD = 6, N = 5;
        const barW = ICON * N + PAD * 2, barH = ICON + PAD * 2;
        return { x: (r.x + r.w) * tileSize - barW, y: r.y * tileSize - barH - 4, w: barW, h: barH, iconW: ICON, pad: PAD, n: N };
    }

    static renderTilePreview(def, atlas, size = 40) {
        const canvas = document.createElement('canvas');
        canvas.width = canvas.height = size;
        const ctx = canvas.getContext('2d');
        ctx.imageSmoothingEnabled = false;
        if (atlas?.loaded) {
            ctx.drawImage(atlas.image, def.atlasOffset.x, def.atlasOffset.y, def.atlasSize.x, def.atlasSize.y, 0, 0, size, size);
        } else {
            ctx.fillStyle = '#1c2130'; ctx.fillRect(0, 0, size, size);
            ctx.fillStyle = '#3d5070'; ctx.font = '10px monospace'; ctx.textAlign = 'center'; ctx.textBaseline = 'middle';
            ctx.fillText('?', size/2, size/2);
        }
        return canvas;
    }
}


/* ================================================================
   TILE CATALOG
================================================================ */
class TileCatalog {
    constructor() {
        this.defs    = new Map();
        this.atlases = new Map();
        this._buildDefaultCatalog();
    }

    _registerAtlas(id, src, tileW = 32, tileH = 32) {
        this.atlases.set(id, new TileAtlas(id, src, new Vector2(tileW, tileH)));
    }

    _registerTile(opts) {
        const def = new TileDefinition(opts);
        this.defs.set(def.id, def);
        return def;
    }

    _registerGrid(atlasId, category, subcategory, cols, rows, tileW, tileH, namePrefix) {
        for (let row = 0; row < rows; row++)
            for (let col = 0; col < cols; col++)
                this._registerTile({
                    id: `${atlasId}_${subcategory}_${col}_${row}`,
                    atlasId,
                    atlasOffset: new Vector2(col * tileW, row * tileH),
                    atlasSize:   new Vector2(tileW, tileH),
                    label:       `${namePrefix} ${row * cols + col + 1}`,
                    category, subcategory,
                });
    }

    _buildDefaultCatalog() {
        // BIG BUILDINGS
        this._registerAtlas('city.buildings.big_buildings.big_building',      'tile_atlases/city/buildings/big_buildings/big_building.png',      32, 32);
        this._registerAtlas('city.buildings.big_buildings.casino',             'tile_atlases/city/buildings/big_buildings/casino.png',             32, 32);
        this._registerAtlas('city.buildings.big_buildings.city_building_01',  'tile_atlases/city/buildings/big_buildings/city_building_01.png',  32, 32);
        this._registerAtlas('city.buildings.big_buildings.city_building_02',  'tile_atlases/city/buildings/big_buildings/city_building_02.png',  32, 32);
        this._registerAtlas('city.buildings.big_buildings.city_building_03',  'tile_atlases/city/buildings/big_buildings/city_building_03.png',  32, 32);
        this._registerAtlas('city.buildings.big_buildings.cool_buildings',    'tile_atlases/city/buildings/big_buildings/cool_buildings.png',    32, 32);
        this._registerAtlas('city.buildings.big_buildings.harbor',            'tile_atlases/city/buildings/big_buildings/harbor.png',            32, 32);
        this._registerAtlas('city.buildings.big_buildings.harbor_building',   'tile_atlases/city/buildings/big_buildings/harbor_building.png',   32, 32);
        this._registerAtlas('city.buildings.big_buildings.another_harbor',    'tile_atlases/city/buildings/big_buildings/another_harbor.png',    32, 32);
        this._registerAtlas('city.buildings.big_buildings.hill_tower',        'tile_atlases/city/buildings/big_buildings/hill_tower.png',        32, 32);
        this._registerAtlas('city.buildings.big_buildings.mall',              'tile_atlases/city/buildings/big_buildings/mall.png',              32, 32);
        this._registerAtlas('city.buildings.big_buildings.mountain_shack',    'tile_atlases/city/buildings/big_buildings/mountain_shack.png',    32, 32);
        this._registerAtlas('city.buildings.big_buildings.museum',            'tile_atlases/city/buildings/big_buildings/museum.png',            32, 32);
        this._registerAtlas('city.buildings.big_buildings.safari_entrance',   'tile_atlases/city/buildings/big_buildings/safari_entrance.png',   32, 32);
        this._registerAtlas('city.buildings.big_buildings.sea_museum',        'tile_atlases/city/buildings/big_buildings/sea_museum.png',        32, 32);
        this._registerAtlas('city.buildings.big_buildings.teleferic_station', 'tile_atlases/city/buildings/big_buildings/teleferic_station.png', 32, 32);
        this._registerAtlas('city.buildings.big_buildings.weather_institute', 'tile_atlases/city/buildings/big_buildings/weather_institute.png', 32, 32);
        this._registerAtlas('city.buildings.big_buildings.wide_building',     'tile_atlases/city/buildings/big_buildings/wide_building.png',     32, 32);
        // HOUSES
        this._registerAtlas('city.buildings.houses.basic_house_01',   'tile_atlases/city/buildings/houses/basic_house_01.png',   32, 32);
        this._registerAtlas('city.buildings.houses.basic_house_02',   'tile_atlases/city/buildings/houses/basic_house_02.png',   32, 32);
        this._registerAtlas('city.buildings.houses.beach_house',      'tile_atlases/city/buildings/houses/beach_house.png',      32, 32);
        this._registerAtlas('city.buildings.houses.big_house',        'tile_atlases/city/buildings/houses/big_house.png',        32, 32);
        this._registerAtlas('city.buildings.houses.big_house_chimney','tile_atlases/city/buildings/houses/big_house_chimney.png',32, 32);
        this._registerAtlas('city.buildings.houses.classic_house',    'tile_atlases/city/buildings/houses/classic_house.png',    32, 32);
        this._registerAtlas('city.buildings.houses.coast_house',      'tile_atlases/city/buildings/houses/coast_house.png',      32, 32);
        this._registerAtlas('city.buildings.houses.exotic_house',     'tile_atlases/city/buildings/houses/exotic_house.png',     32, 32);
        this._registerAtlas('city.buildings.houses.field_house',      'tile_atlases/city/buildings/houses/field_house.png',      32, 32);
        this._registerAtlas('city.buildings.houses.happy_house',      'tile_atlases/city/buildings/houses/happy_house.png',      32, 32);
        this._registerAtlas('city.buildings.houses.island_house',     'tile_atlases/city/buildings/houses/island_house.png',     32, 32);
        // TRAINER
        this._registerAtlas('city.buildings.trainer.trainer_buildings','tile_atlases/city/buildings/trainer/trainer_buildings.png',32, 32);
        this._registerAtlas('city.buildings.trainer.battle_house',     'tile_atlases/city/buildings/trainer/battle_house.png',     32, 32);
        this._registerAtlas('city.buildings.trainer.contest_hall',     'tile_atlases/city/buildings/trainer/contest_hall.png',     32, 32);
        // VARIOUS
        this._registerAtlas('city.buildings.various.beach_shack','tile_atlases/city/buildings/various/beach_shack.png',32, 32);
        this._registerAtlas('city.buildings.various.field_shop',  'tile_atlases/city/buildings/various/field_shop.png',  32, 32);
        // MOUNTAIN ROCKS
        this._registerAtlas('mountain.rocks.geo_01','tile_atlases/mountain/rocks/geo_01.png',32,32);
        this._registerAtlas('mountain.rocks.geo_02','tile_atlases/mountain/rocks/geo_02.png',32,32);
        this._registerAtlas('mountain.rocks.geo_03','tile_atlases/mountain/rocks/geo_03.png',32,32);
        this._registerAtlas('mountain.rocks.geo_04','tile_atlases/mountain/rocks/geo_04.png',32,32);
        this._registerAtlas('mountain.rocks.geo_05','tile_atlases/mountain/rocks/geo_05.png',32,32);
        this._registerAtlas('mountain.rocks.edges', 'tile_atlases/mountain/rocks/edges.png', 32,32);
        // PUDDLES
        this._registerAtlas('mountain.puddles.geo_puddle_1','tile_atlases/mountain/puddles/geo_puddle_1.png',32,32);
        this._registerAtlas('mountain.puddles.geo_puddle_2','tile_atlases/mountain/puddles/geo_puddle_2.png',32,32);
        this._registerAtlas('mountain.puddles.geo_puddle_3','tile_atlases/mountain/puddles/geo_puddle_3.png',32,32);
        this._registerAtlas('mountain.puddles.geo_puddle_4','tile_atlases/mountain/puddles/geo_puddle_4.png',32,32);
        this._registerAtlas('mountain.puddles.geo_puddle_5','tile_atlases/mountain/puddles/geo_puddle_5.png',32,32);
        this._registerAtlas('mountain.puddles.lava_pool',   'tile_atlases/mountain/puddles/lava_pool.png',   32,32);
        this._registerAtlas('mountain.puddles.puddle_water','tile_atlases/mountain/puddles/puddle_water.png',32,32);
        // GROUND
        this._registerAtlas('mountain.ground.earth_patch',    'tile_atlases/mountain/ground/earth_patch.png',    32,32);
        this._registerAtlas('mountain.ground.m_tall_grass',   'tile_atlases/mountain/ground/m_tall_grass.png',   32,32);
        this._registerAtlas('mountain.ground.meteor_site',    'tile_atlases/mountain/ground/meteor_site.png',    32,32);
        this._registerAtlas('mountain.ground.planted_terrain','tile_atlases/mountain/ground/planted_terrain.png',32,32);
        this._registerAtlas('mountain.ground.terrain_path_01','tile_atlases/mountain/ground/terrain_path_01.png',32,32);
        this._registerAtlas('mountain.ground.terrain_path_02','tile_atlases/mountain/ground/terrain_path_02.png',32,32);
        this._registerAtlas('mountain.ground.terrain_path_03','tile_atlases/mountain/ground/terrain_path_03.png',32,32);
        this._registerAtlas('mountain.ground.terrain_path_04','tile_atlases/mountain/ground/terrain_path_04.png',32,32);
        this._registerAtlas('mountain.ground.terrain_path_05','tile_atlases/mountain/ground/terrain_path_05.png',32,32);
        this._registerAtlas('mountain.ground.terrains',       'tile_atlases/mountain/ground/terrains.png',       32,32);
        this._registerAtlas('mountain.ground.terrains_2',     'tile_atlases/mountain/ground/terrains_2.png',     32,32);
        // CAVE
        this._registerAtlas('mountain.cave.entries','tile_atlases/mountain/cave/entries.png',32,32);
        this._registerAtlas('mountain.cave.inside', 'tile_atlases/mountain/cave/inside.png', 32,32);
        // GRASS
        this._registerAtlas('plants.grass.grasses',      'tile_atlases/plants/grass/grasses.png',      32,32);
        this._registerAtlas('plants.grass.tall_grass',   'tile_atlases/plants/grass/tall_grass.png',   32,32);
        this._registerAtlas('plants.grass.brick_path_01','tile_atlases/plants/grass/brick_path_01.png',32,32);
        this._registerAtlas('plants.grass.brick_path_02','tile_atlases/plants/grass/brick_path_02.png',32,32);
        this._registerAtlas('plants.grass.grass_path_01','tile_atlases/plants/grass/grass_path_01.png',32,32);
        this._registerAtlas('plants.grass.grass_path_02','tile_atlases/plants/grass/grass_path_02.png',32,32);
        this._registerAtlas('plants.grass.grass_path_03','tile_atlases/plants/grass/grass_path_03.png',32,32);
        this._registerAtlas('plants.grass.grass_path_04','tile_atlases/plants/grass/grass_path_04.png',32,32);
        this._registerAtlas('plants.grass.grass_path_05','tile_atlases/plants/grass/grass_path_05.png',32,32);
        this._registerAtlas('plants.grass.park_path_01', 'tile_atlases/plants/grass/park_path_01.png', 32,32);
        this._registerAtlas('plants.grass.park_path_02', 'tile_atlases/plants/grass/park_path_02.png', 32,32);
        this._registerAtlas('plants.grass.park_path_03', 'tile_atlases/plants/grass/park_path_03.png', 32,32);
        this._registerAtlas('plants.grass.park_path_04', 'tile_atlases/plants/grass/park_path_04.png', 32,32);
        this._registerAtlas('plants.grass.shadowed_grass','tile_atlases/plants/grass/shadowed_grass.png',32,32);
        // TREES
        this._registerAtlas('plants.trees.base_bush',          'tile_atlases/plants/trees/base_bush.png',          32,32);
        this._registerAtlas('plants.trees.base_tree',          'tile_atlases/plants/trees/base_tree.png',          32,32);
        this._registerAtlas('plants.trees.greater_trees',      'tile_atlases/plants/trees/greater_trees.png',      32,32);
        this._registerAtlas('plants.trees.jungle_trees',       'tile_atlases/plants/trees/jungle_trees.png',       32,32);
        this._registerAtlas('plants.trees.medium_trees',       'tile_atlases/plants/trees/medium_trees.png',       32,32);
        this._registerAtlas('plants.trees.minor_trees',        'tile_atlases/plants/trees/minor_trees.png',        32,32);
        this._registerAtlas('plants.trees.shadowed_trees',     'tile_atlases/plants/trees/shadowed_trees.png',     32,32);
        this._registerAtlas('plants.trees.shadowed_trees_2',   'tile_atlases/plants/trees/shadowed_trees_2.png',   32,32);
        this._registerAtlas('plants.trees.sturdy_tree',        'tile_atlases/plants/trees/sturdy_tree.png',        32,32);
        this._registerAtlas('plants.trees.sturdy_tree_tiling', 'tile_atlases/plants/trees/sturdy_tree_tiling.png', 32,32);
        this._registerAtlas('plants.trees.warm_tree_tiling',   'tile_atlases/plants/trees/warm_tree_tiling.png',   32,32);
        // PROPS
        this._registerAtlas('props.town_props',   'tile_atlases/props/town_props.png',   32,32);
        this._registerAtlas('props.beach_props',  'tile_atlases/props/beach.png',        32,32);
        this._registerAtlas('props.bicycle_path', 'tile_atlases/props/bicycle_path.png', 32,32);
        this._registerAtlas('props.fences',       'tile_atlases/props/fences.png',       32,32);
        this._registerAtlas('props.harbors',      'tile_atlases/props/harbors.png',      32,32);
        this._registerAtlas('props.machines',     'tile_atlases/props/machines.png',     32,32);
        this._registerAtlas('props.teleferic',    'tile_atlases/props/teleferic.png',    32,32);
        this._registerAtlas('props.thermals',     'tile_atlases/props/thermals.png',     32,32);
        this._registerAtlas('props.walls',        'tile_atlases/props/walls.png',        32,32);
    }

    grouped(filter = '') {
        const lf = filter.toLowerCase(), out = {};
        for (const def of this.defs.values()) {
            if (lf && !def.label.toLowerCase().includes(lf) && !def.subcategory.toLowerCase().includes(lf) && !def.category.toLowerCase().includes(lf)) continue;
            if (!out[def.category]) out[def.category] = {};
            if (!out[def.category][def.subcategory]) out[def.category][def.subcategory] = [];
            out[def.category][def.subcategory].push(def);
        }
        return out;
    }
    getAtlas(id) { return this.atlases.get(id) || null; }
    getDef(id)   { return this.defs.get(id)    || null; }
}


/* ================================================================
   PALETTE UI
================================================================ */
class PaletteUI {
    constructor(panelEl, backdropEl, catalog, onSelectStamp) {
        this.panel         = panelEl;
        this.backdrop      = backdropEl;
        this.catalog       = catalog;
        this.onSelectStamp = onSelectStamp;
        this.visible       = false;
        this.searchEl      = panelEl.querySelector('#palette-search');
        this.treeEl        = panelEl.querySelector('#palette-tree');
        this._buildTree();
        this.searchEl.addEventListener('input', () => this._filterTree());
        this.backdrop.addEventListener('click', () => this.hide());
    }

    toggle() { this.visible ? this.hide() : this.show(); }
    show()   { this.visible = true;  this.panel.classList.remove('palette-hidden'); this.backdrop.classList.add('active'); this.searchEl.focus(); }
    hide()   { this.visible = false; this.panel.classList.add('palette-hidden');    this.backdrop.classList.remove('active'); }

    _buildTree() {
        this.treeEl.innerHTML = '';
        const groups = this._groupByTopCategory();
        for (const [topCat, subs] of Object.entries(groups))
            this._addTopCategory(topCat, this._getCategoryIcon(topCat), subs);
    }

    _groupByTopCategory() {
        const groups = {};
        for (const id of this.catalog.atlases.keys()) {
            const parts  = id.split('.');
            const topCat = parts[0];
            if (!groups[topCat]) groups[topCat] = {};
            let cur = groups[topCat];
            for (let i = 1; i < parts.length - 1; i++) { if (!cur[parts[i]]) cur[parts[i]] = {}; cur = cur[parts[i]]; }
            if (!cur._atlases) cur._atlases = [];
            cur._atlases.push(id);
        }
        return groups;
    }

    _addTopCategory(name, icon, data) {
        const cat    = document.createElement('div'); cat.className = 'tree-category';
        const header = document.createElement('div'); header.className = 'tree-cat-header';
        header.innerHTML = `<span class="tree-arrow">▶</span><span class="tree-cat-icon">${icon}</span><span>${name.toUpperCase()}</span>`;
        const body = document.createElement('div'); body.className = 'tree-cat-body';
        header.addEventListener('click', () => { header.classList.toggle('open'); body.classList.toggle('open'); });
        this._renderSubGroups(body, data);
        cat.append(header, body);
        this.treeEl.appendChild(cat);
    }

    _renderSubGroups(parent, data) {
        if (data._atlases) { data._atlases.forEach(id => parent.appendChild(this._createAtlasViewport(id.split('.').pop().replace(/_/g,' '), id))); return; }
        for (const [key, value] of Object.entries(data)) {
            if (key === '_atlases') continue;
            const subHeader = document.createElement('div'); subHeader.className = 'tree-sub-header';
            subHeader.innerHTML = `<span class="tree-arrow">▶</span><span>${key.toUpperCase().replace(/_/g,' ')}</span>`;
            const subBody = document.createElement('div'); subBody.className = 'tree-cat-body';
            subHeader.addEventListener('click', e => { e.stopPropagation(); subHeader.classList.toggle('open'); subBody.classList.toggle('open'); });
            this._renderSubGroups(subBody, value);
            const wrapper = document.createElement('div');
            wrapper.append(subHeader, subBody);
            parent.appendChild(wrapper);
        }
    }

    _createAtlasViewport(displayName, atlasId) {
        const atlas = this.catalog.atlases.get(atlasId);
        if (!atlas) { const p = document.createElement('div'); p.textContent = `Missing: ${displayName}`; p.style.color = '#f44336'; return p; }
        const section  = document.createElement('div'); section.className = 'atlas-section';
        section.innerHTML = `
            <div class="atlas-label">${displayName}</div>
            <div class="atlas-viewport" data-atlas-id="${atlasId}">
                <img src="${atlas.image.src}" alt="${displayName}" draggable="false">
                <div class="atlas-selector"></div>
                <div class="atlas-drag-rect"></div>
            </div>`;
        const viewport = section.querySelector('.atlas-viewport');
        const img      = viewport.querySelector('img');
        const selector = viewport.querySelector('.atlas-selector');
        const dragRect = viewport.querySelector('.atlas-drag-rect');
        img.draggable  = false;
        img.addEventListener('dragstart', e => e.preventDefault());
        this._setupInteraction(viewport, img, selector, dragRect, atlas, atlasId);
        return section;
    }

    _setupInteraction(viewport, img, selector, dragRect, atlas, atlasId) {
        let displayScale = 1;
        const updateScale = () => { if (!img.complete || img.naturalWidth === 0 || img.clientWidth === 0) return; displayScale = img.clientWidth / img.naturalWidth; };
        const forceUpdateScale = () => { updateScale(); setTimeout(updateScale, 30); setTimeout(updateScale, 120); };
        img.addEventListener('load', forceUpdateScale);
        if (img.complete) setTimeout(forceUpdateScale, 10);
        const subHeader = viewport.closest('.tree-sub-header') || viewport.closest('.tree-cat-header');
        if (subHeader) { const obs = new MutationObserver(() => { if (subHeader.classList.contains('open')) forceUpdateScale(); }); obs.observe(subHeader, { attributes: true, attributeFilter: ['class'] }); }

        let isDragging = false, startTile = null;
        const getTile = e => { const r = img.getBoundingClientRect(), ets = 32 * displayScale; return new Vector2(Math.max(0,Math.floor((e.clientX-r.left)/ets)), Math.max(0,Math.floor((e.clientY-r.top)/ets))); };
        const updateSelector = e => { const tile = getTile(e), size = 32*displayScale; selector.style.cssText = `left:${tile.x*size}px;top:${tile.y*size}px;width:${size}px;height:${size}px;display:block`; };

        viewport.addEventListener('mousedown', e => {
            if (e.button !== 0) return; e.preventDefault(); forceUpdateScale();
            isDragging = true; startTile = getTile(e);
            const size = 32 * displayScale;
            dragRect.style.cssText = `left:${startTile.x*size}px;top:${startTile.y*size}px;width:${size}px;height:${size}px;display:block`;
            selector.style.display = 'none';
        });
        viewport.addEventListener('mousemove', e => {
            if (!atlas.loaded) return;
            if (isDragging) {
                const cur = getTile(e), minP = startTile.min(cur), maxP = startTile.max(cur), size = 32*displayScale;
                dragRect.style.cssText = `left:${minP.x*size}px;top:${minP.y*size}px;width:${(maxP.x-minP.x+1)*size}px;height:${(maxP.y-minP.y+1)*size}px;display:block`;
            } else updateSelector(e);
        });
        const finishDrag = e => {
            if (!isDragging) return; isDragging = false;
            const endTile = getTile(e), minP = startTile.min(endTile);
            this.onSelectStamp(new TileStamp(atlasId, minP.scale(32), endTile.x-minP.x+1, endTile.y-minP.y+1));
            dragRect.style.display = 'none'; selector.style.display = 'none';
        };
        viewport.addEventListener('mouseup', finishDrag);
        viewport.addEventListener('mouseleave', finishDrag);
    }

    _filterTree() {
        const term = this.searchEl.value.toLowerCase().trim();
        this.treeEl.querySelectorAll('.atlas-section, .tree-category, .tree-sub-header').forEach(el => {
            el.style.display = term === '' || el.textContent.toLowerCase().includes(term) ? '' : 'none';
        });
    }

    _getCategoryIcon(cat) { return { city:'🏙', mountain:'⛰', plants:'🌱', props:'📦' }[cat] || '📁'; }
}


/* ================================================================
   TOOL MANAGER
================================================================ */
const TOOLS = Object.freeze({ BRUSH:'brush', ERASER:'eraser', FILL:'fill', SELECT:'select', EYEDROPPER:'eyedropper' });

class ToolManager {
    constructor() {
        this.activeTool       = TOOLS.BRUSH;
        this.isPainting       = false;
        this._paintedInStroke = new Set();
    }

    setTool(tool) { this.activeTool = tool; }

    onPointerDown(tilePos, map, catalog, activeStamp, history, selection, collisionMode, activeCollision, activeCollisionTool) {
        this.isPainting = true;
        this._paintedInStroke.clear();

        if (collisionMode) {
            if      (activeCollisionTool === 'eraser') this._eraseCollision(tilePos, map, history);
            else if (activeCollisionTool === 'fill')   this._floodFillCollision(tilePos, map, activeCollision, history);
            else                                       this._applyCollision(tilePos, map, activeCollision, history);
            return;
        }

        switch (this.activeTool) {
            case TOOLS.BRUSH:      this._paint(tilePos, map, catalog, activeStamp, history); break;
            case TOOLS.ERASER:     this._erase(tilePos, map, history); break;
            case TOOLS.FILL:       this._floodFill(tilePos, map, catalog, activeStamp, history); break;
            case TOOLS.SELECT:     selection.begin(tilePos); break;
            case TOOLS.EYEDROPPER: this._eyedropper(tilePos, map, catalog); break;
        }
    }

    onPointerMove(tilePos, map, catalog, activeStamp, history, selection, collisionMode, activeCollision, activeCollisionTool) {
        if (!this.isPainting) return;

        if (collisionMode) {
            if      (activeCollisionTool === 'eraser') this._eraseCollision(tilePos, map, history);
            else if (activeCollisionTool === 'brush')  this._applyCollision(tilePos, map, activeCollision, history);
            // fill is click-only, intentionally omitted here
            return;
        }

        switch (this.activeTool) {
            case TOOLS.BRUSH:  this._paint(tilePos, map, catalog, activeStamp, history); break;
            case TOOLS.ERASER: this._erase(tilePos, map, history); break;
            case TOOLS.SELECT: selection.update(tilePos); break;
        }
    }

    onPointerUp(selection) {
        this.isPainting = false;
        if (this.activeTool === TOOLS.SELECT) selection.finish();
    }

    _key(pos) { return `${pos.x},${pos.y}`; }

    _paint(tilePos, map, catalog, activeStamp, history) {
        if (!map.isInBounds(tilePos) || !activeStamp) return;
        const k = this._key(tilePos);
        if (this._paintedInStroke.has(k)) return;
        this._paintedInStroke.add(k);
        const layer    = map.activeLayer;
        const commands = [];
        for (const newTile of activeStamp.getTiles(tilePos, layer.index, catalog)) {
            if (!map.isInBounds(newTile.tilePos)) continue;
            commands.push(new PaintCommand(layer, newTile, layer.getTile(newTile.tilePos)));
        }
        if (commands.length) history.push(new BatchCommand(commands));
    }

    _erase(tilePos, map, history) {
        if (!map.isInBounds(tilePos)) return;
        const k = this._key(tilePos);
        if (this._paintedInStroke.has(k)) return;
        this._paintedInStroke.add(k);
        const oldTile = map.activeLayer.getTile(tilePos);
        if (oldTile) history.push(new PaintCommand(map.activeLayer, null, oldTile));
    }

    _floodFill(startPos, map, catalog, activeStamp, history) {
        if (!map.isInBounds(startPos) || !activeStamp) return;
        if (activeStamp.width !== 1 || activeStamp.height !== 1) return;
        const layer       = map.activeLayer;
        const targetTile  = layer.getTile(startPos);
        const targetDefId = targetTile ? targetTile.defId : null;
        const stampTiles  = activeStamp.getTiles(startPos, layer.index, catalog);
        if (!stampTiles.length) return;
        const newDef = catalog.getDef(stampTiles[0].defId);
        if (!newDef || targetDefId === newDef.id) return;
        const commands = [], visited = new Set(), queue = [startPos.clone()];
        while (queue.length) {
            const pos = queue.shift(), key = this._key(pos);
            if (visited.has(key) || !map.isInBounds(pos)) continue;
            const existing = layer.getTile(pos);
            if ((existing ? existing.defId : null) !== targetDefId) continue;
            visited.add(key);
            commands.push(new PaintCommand(layer, new Tile(newDef, pos.clone(), layer.index), existing));
            queue.push(pos.add(new Vector2(1,0)), pos.add(new Vector2(-1,0)), pos.add(new Vector2(0,1)), pos.add(new Vector2(0,-1)));
            if (visited.size > 15000) break;
        }
        if (commands.length) history.push(new BatchCommand(commands));
    }

    _applyCollision(tilePos, map, activeCollision, history) {
        if (!map.isInBounds(tilePos)) return;
        const k = this._key(tilePos);
        if (this._paintedInStroke.has(k)) return;
        this._paintedInStroke.add(k);
        const tile = map.activeLayer.getTile(tilePos);
        if (!tile || tile.collision === activeCollision) return;
        const oldCol = tile.collision, newCol = activeCollision;
        const cmd = { execute: () => { tile.collision = newCol; }, undo: () => { tile.collision = oldCol; } };
        cmd.execute();
        history.undoStack.push(cmd); history.redoStack = [];
    }

    // Collision eraser — resets collision to NONE
    _eraseCollision(tilePos, map, history) {
        if (!map.isInBounds(tilePos)) return;
        const k = this._key(tilePos);
        if (this._paintedInStroke.has(k)) return;
        this._paintedInStroke.add(k);
        const tile = map.activeLayer.getTile(tilePos);
        if (!tile || tile.collision === 'NONE') return;
        const oldCol = tile.collision;
        const cmd = { execute: () => { tile.collision = 'NONE'; }, undo: () => { tile.collision = oldCol; } };
        cmd.execute();
        history.undoStack.push(cmd); history.redoStack = [];
    }
    
    _floodFillCollision(startPos, map, targetCollision, history) {
        if (!map.isInBounds(startPos)) return;

        const layer         = map.activeLayer;
        const seedTile      = layer.getTile(startPos);
        const seedCollision = seedTile ? seedTile.collision : 'NONE';

        if (seedCollision === targetCollision) return;

        const commands = [];
        const visited  = new Set();
        const queue    = [startPos.clone()];

        while (queue.length) {
            const pos = queue.shift();
            const key = `${pos.x},${pos.y}`;
            if (visited.has(key) || !map.isInBounds(pos)) continue;

            const tile          = layer.getTile(pos);
            const tileCollision = tile ? tile.collision : 'NONE';

            if (tileCollision !== seedCollision) continue;

            visited.add(key);

            if (tile) {
                const oldCol = tile.collision, newCol = targetCollision;
                commands.push({
                    execute: () => { tile.collision = newCol; },
                    undo:    () => { tile.collision = oldCol; },
                });
            }

            queue.push(
                new Vector2(pos.x + 1, pos.y), new Vector2(pos.x - 1, pos.y),
                new Vector2(pos.x, pos.y + 1), new Vector2(pos.x, pos.y - 1)
            );

            if (visited.size > 15000) break;
        }

        if (!commands.length) return;

        const batch = {
            execute: () => commands.forEach(c => c.execute()),
            undo:    () => [...commands].reverse().forEach(c => c.undo()),
        };
        batch.execute();
        history.undoStack.push(batch);
        history.redoStack = [];
    }

    _eyedropper(tilePos, map, catalog) {
        if (!map.isInBounds(tilePos)) return;
        const tile = map.activeLayer.getTile(tilePos);
        if (!tile) return;
        const def = catalog.getDef(tile.defId);
        if (!def) return;
        if (this.onEyedropperPick) this.onEyedropperPick(new TileStamp(def.atlasId, def.atlasOffset.clone(), 1, 1));
    }
}


/* ================================================================
   APP
================================================================ */
class App {
    constructor() {
        this.catalog   = new TileCatalog();
        this.map       = new TileMap('Untitled Map', new Vector2(20, 15), 32);
        this.renderer  = null;
        this.history   = new HistoryManager();
        this.selection = new SelectionManager();

        this.toolMgr = new ToolManager();
        this.toolMgr.onEyedropperPick = (stamp) => {
            this.activeStamp = stamp;
            this._updateActiveTilePreview();
            this._showToast(`Picked: ${stamp.width}×${stamp.height} tile`);
            this._scheduleRender();
        };

        this.paletteUI           = null;
        this.activeStamp         = null;
        this.showCollision       = false;
        this.activeCollision     = 'NONE';
        this.activeCollisionTool = 'brush';   // 'brush' | 'eraser'
        this.hoverTile           = null;
        this._rafPending         = false;
        this._hoveredActionIndex = -1;
    }

    init() {
        const canvas = document.getElementById('map-canvas');
        this.renderer = new Renderer(canvas);
        this._applyMapToCanvas();

        this.paletteUI = new PaletteUI(
            document.getElementById('tile-palette'),
            document.getElementById('palette-backdrop'),
            this.catalog,
            (stamp) => { this.activeStamp = stamp; this._updateActiveTilePreview(); }
        );

        this._bindTopbar();
        this._bindTools();
        this._bindCanvas();
        this._bindKeyboard();
        this._bindLayers();
        this._bindCollision();

        this._scheduleRender();
        this._showToast('Welcome! Press TAB to open tile palette.');
    }

    _scheduleRender() {
        if (this._rafPending) return;
        this._rafPending = true;
        requestAnimationFrame(() => {
            this._rafPending = false;
            let movePreview = null;
            if (this.selection.isMoving && this.selection._moveCache) {
                const { layer, tiles, oldOrigin } = this.selection._moveCache;
                const delta    = new Vector2(this.selection.start.x - oldOrigin.x, this.selection.start.y - oldOrigin.y);
                const tileKeys = new Set(tiles.map(t => `${t.tilePos.x},${t.tilePos.y}`));
                movePreview    = { layer, tiles, delta, tileKeys };
            }
            this.renderer.render(
                this.map, this.catalog.atlases, this.showCollision,
                this.selection, this.hoverTile, this.activeStamp,
                this.toolMgr.activeTool, this._hoveredActionIndex, movePreview,
                this.activeCollision, this.activeCollisionTool
            );
        });
    }

    _applyMapToCanvas() { this.renderer.resize(this.map.pixelSize); this._scheduleRender(); }

    _bindTopbar() {
        document.getElementById('map-config-form').addEventListener('submit', e => {
            e.preventDefault();
            const name = document.getElementById('cfg-name').value.trim() || 'Untitled Map';
            const w    = Math.max(4, Math.min(200, parseInt(document.getElementById('cfg-width').value)    || 20));
            const h    = Math.max(4, Math.min(200, parseInt(document.getElementById('cfg-height').value)   || 15));
            const ts   = Math.max(8, Math.min(128, parseInt(document.getElementById('cfg-tilesize').value) || 32));
            this.map.name = name; this.map.resize(new Vector2(w, h), ts); this._applyMapToCanvas();
            this._showToast(`Map resized: ${w}×${h} @ ${ts}px`);
        });
        document.getElementById('btn-export').addEventListener('click', () => this._exportJSON());
        document.getElementById('btn-clear').addEventListener('click', () => {
            if (!confirm('Clear all tiles? This cannot be undone.')) return;
            this.map.layers.forEach(l => l.clear()); this.history.clear(); this._scheduleRender(); this._showToast('Map cleared.');
        });
    }

    _bindTools() {
        // Draw tool buttons
        document.querySelectorAll('.tool-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                document.querySelectorAll('.tool-btn').forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                this.toolMgr.setTool(btn.dataset.tool);
                if (btn.dataset.tool !== 'select') { this.selection.clear(); this._scheduleRender(); }
            });
        });

        // Draw / Collision tab switcher
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
                document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
                btn.classList.add('active');
                document.getElementById(`tab-${btn.dataset.tab}`).classList.add('active');
                this.showCollision = btn.dataset.tab === 'collision';
                this._scheduleRender();
            });
        });

        // Collision-specific tool buttons
        document.querySelectorAll('.collision-tool-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                document.querySelectorAll('.collision-tool-btn').forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                this.activeCollisionTool = btn.dataset.collisionTool;
                this._scheduleRender();
            });
        });
    }

    _getActionBarHit(pixelPos) {
        if (!this.selection.active) return -1;
        const bar = Renderer.getActionBarRect(this.selection, this.map.tileSize);
        if (!bar) return -1;
        if (pixelPos.x < bar.x || pixelPos.x > bar.x + bar.w || pixelPos.y < bar.y || pixelPos.y > bar.y + bar.h) return -1;
        const index = Math.floor((pixelPos.x - bar.x - bar.pad) / bar.iconW);
        return (index >= 0 && index < bar.n) ? index : -1;
    }

    _handleSelectionAction(pixelPos) {
        const index = this._getActionBarHit(pixelPos);
        if (index === -1) return false;
        const tp = pixelPos.scale(1 / this.map.tileSize).floor();
        switch (index) {
            case 0: this.selection.startMove(tp, this.map.activeLayer); this._showToast('Drag to move selection'); break;
            case 1: this._rotateSelection(90);  break;
            case 2: this._rotateSelection(-90); break;
            case 3: this._deleteSelection();    break;
            case 4: this.selection.clear(); this._scheduleRender(); break;
        }
        return true;
    }

    _bindCanvas() {
        const canvas = this.renderer.canvas;
        let lastKey  = null;
        const getPixelPos  = e => { const r = canvas.getBoundingClientRect(); return new Vector2(e.clientX - r.left, e.clientY - r.top); };
        const getTilePos   = px => px.scale(1 / this.map.tileSize).floor();
        const updateCoords = tp => {
            const tile = tp ? this.map.activeLayer.getTile(tp) : null;
            document.getElementById('canvas-coords').textContent = tp
                ? `Tile: ${tp.x}, ${tp.y}${tile ? ` | ${tile.defId} [${tile.collision}]` : ''}` : '';
        };

        canvas.addEventListener('mousedown', e => {
            if (e.button !== 0) return;
            e.preventDefault();
            const px = getPixelPos(e), tp = getTilePos(px);
            this.hoverTile = tp;
            if (this._handleSelectionAction(px)) { this._scheduleRender(); return; }
            this.toolMgr.onPointerDown(tp, this.map, this.catalog, this.activeStamp, this.history, this.selection, this.showCollision, this.activeCollision, this.activeCollisionTool);
            this._scheduleRender(); updateCoords(tp);
        });

        canvas.addEventListener('mousemove', e => {
            const px = getPixelPos(e), tp = getTilePos(px), key = `${tp.x},${tp.y}`;
            this.hoverTile = tp;
            const prev = this._hoveredActionIndex;
            this._hoveredActionIndex = this._getActionBarHit(px);
            if (this._hoveredActionIndex !== prev) this._scheduleRender();
            if (this.selection.isMoving) { this.selection.updateMove(tp); this._scheduleRender(); updateCoords(tp); return; }
            if (key !== lastKey) {
                lastKey = key;
                this.toolMgr.onPointerMove(tp, this.map, this.catalog, this.activeStamp, this.history, this.selection, this.showCollision, this.activeCollision, this.activeCollisionTool);
                this._scheduleRender(); updateCoords(tp);
            }
        });

        window.addEventListener('mouseup', e => {
            if (e.button !== 0) return;
            if (this.selection.isMoving) this._commitMove(); else this.toolMgr.onPointerUp(this.selection);
            this._scheduleRender();
        });

        canvas.addEventListener('mouseleave', () => {
            lastKey = null; this.hoverTile = null; this._hoveredActionIndex = -1;
            this._scheduleRender(); document.getElementById('canvas-coords').textContent = '';
        });

        canvas.addEventListener('contextmenu', e => {
            e.preventDefault();
            const px = getPixelPos(e), tp = getTilePos(px);
            if (this.toolMgr.activeTool === TOOLS.BRUSH) {
                this.toolMgr._eyedropper(tp, this.map, this.catalog);
            } else {
                const oldTile = this.map.activeLayer.getTile(tp);
                if (oldTile) { this.history.push(new PaintCommand(this.map.activeLayer, null, oldTile)); this._scheduleRender(); }
            }
        });
    }

    _commitMove() {
        const cache = this.selection._moveCache;
        this.selection.finishMove();
        if (!cache || !cache.tiles.length) return;
        const { layer, tiles, oldOrigin } = cache;
        const delta = new Vector2(this.selection.start.x - oldOrigin.x, this.selection.start.y - oldOrigin.y);
        if (delta.x === 0 && delta.y === 0) return;
        const commands = [];
        for (const tile of tiles) commands.push(new PaintCommand(layer, null, tile));
        for (const tile of tiles) {
            const newPos = tile.tilePos.add(delta);
            if (!this.map.isInBounds(newPos)) continue;
            const def = this.catalog.getDef(tile.defId);
            if (!def) continue;
            const newTile = new Tile(def, newPos, layer.index);
            newTile.collision = tile.collision;
            commands.push(new PaintCommand(layer, newTile, layer.getTile(newPos)));
        }
        if (commands.length) this.history.push(new BatchCommand(commands));
    }

    _bindKeyboard() {
        window.addEventListener('keydown', e => {
            if (e.target.tagName === 'INPUT') return;
            switch (e.key) {
                case 'Tab': case ' ': e.preventDefault(); this.paletteUI.toggle(); break;
                case 'i': case 'I': this._activateToolButton('eyedropper'); break;
                case 'b': case 'B': this._activateToolButton('brush');      break;
                case 'e': case 'E': this._activateToolButton('eraser');     break;
                case 'f': case 'F': this._activateToolButton('fill');       break;
                case 'g': case 'G': this._activateCollisionToolButton('fill'); break;
                case 's': case 'S': this._activateToolButton('select');     break;
                case 'z': case 'Z':
                    if (e.ctrlKey || e.metaKey) { e.shiftKey ? this.history.redo() : this.history.undo(); this._scheduleRender(); }
                    break;
                case 'Escape': this.selection.clear(); this.paletteUI.hide(); this._scheduleRender(); break;
            }
        });
    }

    _activateToolButton(tool) {
        document.querySelectorAll('.tool-btn').forEach(b => b.classList.remove('active'));
        document.querySelector(`.tool-btn[data-tool="${tool}"]`)?.classList.add('active');
        this.toolMgr.setTool(tool);
        if (tool !== 'select') { this.selection.clear(); this._scheduleRender(); }
    }
        
    _activateCollisionToolButton(tool) {
        document.querySelectorAll('.collision-tool-btn').forEach(b => b.classList.remove('active'));
        document.querySelector(`.collision-tool-btn[data-collision-tool="${tool}"]`)?.classList.add('active');
        this.activeCollisionTool = tool;
        this._scheduleRender();
    }

    _bindLayers() {
        const listEl = document.getElementById('layer-list');
        listEl.addEventListener('click', e => {
            const item = e.target.closest('.layer-item');
            if (!item) return;
            if (e.target.classList.contains('layer-eye')) {
                const layer = this.map.layers[parseInt(item.dataset.layer)];
                if (layer) { layer.visible = !layer.visible; e.target.style.opacity = layer.visible ? '1' : '0.3'; this._scheduleRender(); }
                return;
            }
            listEl.querySelectorAll('.layer-item').forEach(i => i.classList.remove('active'));
            item.classList.add('active');
            this.map.activeLayerIndex = parseInt(item.dataset.layer);
            this._scheduleRender();
        });
        document.getElementById('btn-add-layer').addEventListener('click', () => {
            const layer = this.map.addLayer();
            const item  = document.createElement('div');
            item.className = 'layer-item'; item.dataset.layer = layer.index;
            item.innerHTML = `<span class="layer-eye">👁</span><span class="layer-name">${layer.name}</span>`;
            listEl.appendChild(item);
        });
    }

    _bindCollision() {
        document.querySelectorAll('.collision-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                document.querySelectorAll('.collision-btn').forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                this.activeCollision = btn.dataset.collision;
            });
        });
    }

    _updateActiveTilePreview() {
        const previewCanvas = document.getElementById('active-tile-canvas');
        const labelEl       = document.getElementById('active-tile-label');
        const ctx           = previewCanvas.getContext('2d');
        ctx.clearRect(0, 0, 48, 48);
        if (!this.activeStamp) { labelEl.textContent = 'None'; return; }
        const atlas = this.catalog.getAtlas(this.activeStamp.atlasId);
        if (!atlas || !atlas.loaded) { labelEl.textContent = `Stamp ${this.activeStamp.width}×${this.activeStamp.height}`; return; }
        if (this.activeStamp.width === 1 && this.activeStamp.height === 1) {
            const tempDef = new TileDefinition({ id:'temp', atlasId:this.activeStamp.atlasId, atlasOffset:this.activeStamp.startOffset.clone(), atlasSize:this.activeStamp.tileSize.clone(), label:'Selected Tile', category:'', subcategory:'' });
            ctx.drawImage(Renderer.renderTilePreview(tempDef, atlas, 48), 0, 0, 48, 48);
            labelEl.textContent = 'Single Tile';
        } else {
            labelEl.textContent = `${this.activeStamp.width}×${this.activeStamp.height} Stamp`;
            ctx.fillStyle = '#e8a04033'; ctx.fillRect(4, 4, 40, 40);
            ctx.strokeStyle = '#e8a040'; ctx.lineWidth = 2; ctx.strokeRect(6, 6, 36, 36);
            ctx.fillStyle = '#e8a040'; ctx.font = 'bold 11px monospace'; ctx.textAlign = 'center'; ctx.textBaseline = 'middle';
            ctx.fillText(`${this.activeStamp.width}×${this.activeStamp.height}`, 24, 24);
        }
    }

    _exportJSON() {
        const data = JSON.stringify(this.map.toJSON(), null, 2);
        const url  = URL.createObjectURL(new Blob([data], { type:'application/json' }));
        Object.assign(document.createElement('a'), { href:url, download:`${this.map.name.replace(/\s+/g,'_')}.json` }).click();
        URL.revokeObjectURL(url);
        this._showToast('Map exported as JSON.');
    }

    _deleteSelection() {
        if (!this.selection.active) return;
        const r = this.selection.rect, layer = this.map.activeLayer, commands = [];
        for (let y = r.y; y < r.y+r.h; y++)
            for (let x = r.x; x < r.x+r.w; x++) {
                const tile = layer.getTile(new Vector2(x, y));
                if (tile) commands.push(new PaintCommand(layer, null, tile));
            }
        if (commands.length) this.history.push(new BatchCommand(commands));
        this._scheduleRender(); this._showToast('Selection deleted');
    }

    _rotateSelection(degrees) {
        if (!this.selection.active) return;
        const r = this.selection.rect, layer = this.map.activeLayer, commands = [];
        const centerX = r.x + (r.w-1)/2, centerY = r.y + (r.h-1)/2;
        const tiles = [];
        for (let y = r.y; y < r.y+r.h; y++)
            for (let x = r.x; x < r.x+r.w; x++) { const t = layer.getTile(new Vector2(x,y)); if (t) tiles.push(t); }
        tiles.forEach(t => commands.push(new PaintCommand(layer, null, t)));
        tiles.forEach(t => {
            const relX = t.tilePos.x - centerX, relY = t.tilePos.y - centerY;
            const newPos = new Vector2(Math.round(centerX + (degrees===90?relY:-relY)), Math.round(centerY + (degrees===90?-relX:relX)));
            if (this.map.isInBounds(newPos)) {
                const def = this.catalog.getDef(t.defId);
                if (def) { const nt = new Tile(def, newPos, layer.index); nt.collision = t.collision; commands.push(new PaintCommand(layer, nt, layer.getTile(newPos))); }
            }
        });
        if (commands.length) this.history.push(new BatchCommand(commands));
        this._scheduleRender(); this._showToast(`Rotated ${degrees > 0 ? '90° CW' : '90° CCW'}`);
    }

    _showToast(msg) {
        let el = document.getElementById('toast');
        if (!el) { el = document.createElement('div'); el.id = 'toast'; document.body.appendChild(el); }
        el.textContent = msg; el.classList.add('show');
        clearTimeout(this._toastTimer);
        this._toastTimer = setTimeout(() => el.classList.remove('show'), 2600);
    }
}


// ============================================================
//  BOOTSTRAP
// ============================================================
const app = new App();
document.addEventListener('DOMContentLoaded', () => app.init());