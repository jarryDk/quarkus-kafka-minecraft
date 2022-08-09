class PlayersElement extends HTMLElement {

    constructor() {
        super();
        this.root = this.attachShadow({ mode: 'open' });
    }

    connectedCallback() {
        customElements.whenDefined('jarry-player').then(_ => this.populatePosts());
    }

    populatePosts() {
        console.log("populatePosts ... ");
        var source = new EventSource("/players");
        source.onmessage = (event) => {
            console.log("event ... " + event);
            var json = JSON.parse(event.data);
            console.log(json);

            const player = this.querySelector("jarry-player");
            const clonedPlayer = player.cloneNode(true);
            clonedPlayer.player = json;
            this.root.appendChild(clonedPlayer);

        };
    }

};

class PlayerElement extends HTMLElement {

    constructor() {
        super();
        this.root = this.attachShadow({ mode: 'open' });
        this._player = {};
    }

    get player(){
        return this._player;
    }
    set player(player){
        this._player = player;
    }

    connectedCallback() {
        this.root.appendChild(this.template());
        const playerName = this.root.querySelector("[player-name]");
        playerName.innerText = this.player.name;
        const playerX = this.root.querySelector("[player-x]");
        playerX.innerText = this.player.x;
        const playerY = this.root.querySelector("[player-y]");
        playerY.innerText = this.player.y;
        const playerZ = this.root.querySelector("[player-z]");
        playerZ.innerText = this.player.z;
    }

    template() {
        if (!PlayerElement.cachedTemplate) {
            const templateElement = document.createElement("template");
            templateElement.innerHTML = `
            <article>
                <slot name="name" player-name>t</slot>
                ( X : <slot name="x" player-x>x</slot>
                , Y: <slot name="y" player-y>y</slot>
                , Z: <slot name="z" player-z>z</slot> )
            </article>
            `;
            PlayerElement.cachedTemplate = templateElement.content;
        }
        return PlayerElement.cachedTemplate.cloneNode(true);
    }

};

customElements.define("jarry-players", PlayersElement);
customElements.define("jarry-player", PlayerElement);