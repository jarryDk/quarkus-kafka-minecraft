class ChatsElement extends HTMLElement {

    constructor() {
        super();
        this.root = this.attachShadow({ mode: 'open' });
    }

    connectedCallback() {
        customElements
            .whenDefined('jarry-chats')
            .then(_ => this.populatePosts());
    }

    populatePosts() {
        console.log("populatePosts ... ");
        var source = new EventSource("/chats");
        source.onmessage = (event) => {
            console.log("event ... " + event);
            var json = JSON.parse(event.data);
            console.log(json);

            const chat = this.querySelector("jarry-chat");
            const clonedChat = chat.cloneNode(true);
            clonedChat.chat = json;
            let theFirstChild = this.root.firstChild;
            if(theFirstChild != null){
                this.root.insertBefore(clonedChat, theFirstChild);
            }else{
                this.root.appendChild(clonedChat);
            }

        };
    }

};

class ChatElement extends HTMLElement {

    constructor() {
        super();
        this.root = this.attachShadow({ mode: 'open' });
        this._chat = {};
    }

    get chat(){
        return this._chat;
    }
    set chat(chat){
        this._chat = chat;
    }

    connectedCallback() {
        console.info("Chat", this.chat);
        this.root.appendChild(this.template());

        const playerMessage = this.root.querySelector("[player-message]");
        playerMessage.innerText = this.chat.message;

        if( this.chat.player !== undefined ) {
            var player = this.chat.player;
            const playerName = this.root.querySelector("[player-name]");
            playerName.innerText = player.name;
            const playerX = this.root.querySelector("[player-x]");
            playerX.innerText = player.x;
            const playerY = this.root.querySelector("[player-y]");
            playerY.innerText = player.y;
            const playerZ = this.root.querySelector("[player-z]");
            playerZ.innerText = player.z;
        }

    }

    template() {
        if (!ChatElement.cachedTemplate) {
            const templateElement = document.createElement("template");
            templateElement.innerHTML = `
            <article>
                <slot name="name" player-name>t</slot> : 
                <slot name="message" player-message>m</slot>
                <div part="pl" class="player-location">
                ( X : <slot name="x" player-x>x</slot>
                , Y: <slot name="y" player-y>y</slot>
                , Z: <slot name="z" player-z>z</slot> )
                </div>
            </article>
            <style>
            .player-location{
                color: #ccc;
            }
            </style>
            `;
            ChatElement.cachedTemplate = templateElement.content;
        }
        return ChatElement.cachedTemplate.cloneNode(true);
    }

};

class EventEntiesElement extends HTMLElement {

    constructor() {
        super();
        this.root = this.attachShadow({ mode: 'open' });
    }

    connectedCallback() {
        customElements
            .whenDefined('jarry-entities')
            .then(_ => this.populatePosts());
    }

    populatePosts() {
        console.log("populatePosts (EventEntiesElement) ... ");
        var source = new EventSource("/eventEntities");
        source.onmessage = (event) => {
            console.log("event ... ", event);
            var json = JSON.parse(event.data);
            const jarryEntity = this.querySelector("jarry-entity");
            const clonedJarryEntity = jarryEntity.cloneNode(true);
            clonedJarryEntity.entity = json;
            let theFirstChild = this.root.firstChild;
            if(theFirstChild != null){
                this.root.insertBefore(clonedJarryEntity, theFirstChild);
            }else{
                this.root.appendChild(clonedJarryEntity);
            }
        };
    }

};

class EventEntityElement extends HTMLElement {

    constructor() {
        super();
        this.root = this.attachShadow({ mode: 'open' });
        this._entity = {};
    }

    get entity(){
        return this._entity;
    }
    set entity(entity){
        this._entity = entity;
    }

    connectedCallback() {
        this.root.appendChild(this.template());
        const eventEvent = this.root.querySelector("[event-event]");
        console.info(this.entity.event);
        if (this.entity.event == "EntityJoinLevelEvent"){
            eventEvent.innerHTML = '<img src="images/right-arrow.png" /> ';
        } else if (this.entity.event == "EntityLeaveLevelEvent"){
            eventEvent.innerHTML = '<img src="images/left-arrow.png" /> ';
        } else {
            eventEvent.innerText = this.entity.event;
        }
        const eventName = this.root.querySelector("[event-name]");
        eventName.innerText = this.entity.name;
        const eventX = this.root.querySelector("[event-x]");
        eventX.innerText = this.entity.x;
        const eventY = this.root.querySelector("[event-y]");
        eventY.innerText = this.entity.y;
        const eventZ = this.root.querySelector("[event-z]");
        eventZ.innerText = this.entity.z;
    }

    template() {
        if (!EventEntityElement.cachedTemplate) {
            const templateElement = document.createElement("template");
            templateElement.innerHTML = `
            <article>
                <slot name="event" event-event>t</slot>
                <slot name="name" event-name>t</slot>
                <!--
                <div part="pl" class="event-location">
                ( X : <slot name="x" event-x>x</slot>
                , Y: <slot name="y" event-y>y</slot>
                , Z: <slot name="z" event-z>z</slot> )
                </div>
                -->
            </article>
            <style>
            .event-location{
                color: #ccc;
            }
            .right-arrow{
                background-image: url(images/right-arrow.png);
            }
            .left-arrow{
                background-image: url(images/left-arrow.png);
            }
            </style>
            `;
            EventEntityElement.cachedTemplate = templateElement.content;
        }
        return EventEntityElement.cachedTemplate.cloneNode(true);
    }

};


class PlayersElement extends HTMLElement {

    constructor() {
        super();
        this.root = this.attachShadow({ mode: 'open' });
    }

    connectedCallback() {
        customElements
            .whenDefined('jarry-player')
            .then(_ => this.populatePosts());
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
            let theFirstChild = this.root.firstChild;
            if(theFirstChild != null){
                this.root.insertBefore(clonedPlayer, theFirstChild);
            }else{
                this.root.appendChild(clonedPlayer);
            }

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
                <div part="pl" class="player-location">
                ( X : <slot name="x" player-x>x</slot>
                , Y: <slot name="y" player-y>y</slot>
                , Z: <slot name="z" player-z>z</slot> )
                </div>
            </article>
            <style>
            .player-location{
                color: #ccc;
            }
            </style>
            `;
            PlayerElement.cachedTemplate = templateElement.content;
        }
        return PlayerElement.cachedTemplate.cloneNode(true);
    }

};

customElements.define("jarry-chats", ChatsElement);
customElements.define("jarry-chat", ChatElement);
customElements.define("jarry-entities", EventEntiesElement);
customElements.define("jarry-entity", EventEntityElement);
customElements.define("jarry-players", PlayersElement);
customElements.define("jarry-player", PlayerElement);