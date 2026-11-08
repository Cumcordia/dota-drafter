(function () {
  'use strict';

  const API_URL = 'http://127.0.0.1:8080/api/analyze';
  const IMG_DIR = 'img/heroes/';
  const IMG_EXT = '.png';
  const BAR_RANGE = 3;

  const POSITION_TITLES = {
    1: 'Pos 1 · Carry', 2: 'Pos 2 · Mid', 3: 'Pos 3 · Offlane',
    4: 'Pos 4 · Soft support', 5: 'Pos 5 · Hard support'
  };
  const LANES = ['TOP', 'MID', 'BOTTOM'];
  const LANE_TITLES = { TOP: 'Top', MID: 'Mid', BOTTOM: 'Bottom' };

  let HEROES = [];
  let HERO_BY_ID = new Map();
  const norm = id => String(id).replace(/_/g, '-');

  const HERO_IMAGES = {
  'anti-mage': 'https://static.wikia.nocookie.net/dota2_gamepedia/images/8/8e/Anti-Mage_icon.png/revision/latest?cb=20200916215957', 
  'juggernaut': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvGzDX18-URJsLhq2sjDEuZdpfZp03pKp6hWTla1hE_w&s=10', 
  'phantom-assassin': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJGd4wPm-eivTceuhEgI7zQ78zesulSuCIxzbDG5HvpA&s=10', 
  'luna': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT2h6pvNoHytg11WUeOOru4h1ZzTCTxceac7ArEuz4DPA&s=10', 
  'faceless-void': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRaboGB8DZOSu04pqC_kFJYP3CNWms0I9oi3UG0ocVjaQ&s=10', 
  'drow-ranger': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQId5uvG0Fx8uPzZ6IQg4LC61vjYTWOdFU_8YCzgoaC7A&s=10',
  'invoker': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTVyffGBXfYCLm29PE8pdoJ3uE6vA63Tbx_USApRglJiw&s=10', 
  'puck': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSI1FowPbSGt8A2zJeJJCIoOyVP6VhMdWbafRmG2-qgOQ&s=10', 
  'storm-spirit': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZOvUWmJJb70B1DzLSIz2K3QkczQqj5Sf-rsULLZ_5tA&s=10', 
  'queen-of-pain': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxapd7-AqxaA3iVPdQWBO9BulM2WjPHyK69zCGA5E_Lw&s=10', 
  'sniper': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRq7w3Ruxpuzy-qSKETiYh9s0b2S8erbWD2OZT9zg_2Jg&s=10', 
  'shadow-fiend': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRC6sBOhatKJIHLx-iutQDQCBAe8R_XtQDFB1tjcF1r1g&s=10',
  'axe': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQY2sj76kTiFRF1rLS__KjTdCa_NsQbtS8zP5h4Dju1gA&s=10', 
  'mars': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ88hpd0jJbckc3oriEcHmz04hm_eR38uEfUV2CmBvbkw&s=10', 
  'tidehunter': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnGhNM8NrfMaGmvxng1buVs1yIzfOwv3pJ1jpESwhOmg&s=10', 
  'centaur-warrunner': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS0dCtsNrmwNtLiemuo8J4AKLsuCYPhqBTzHZDP5aTVcA&s=10', 
  'underlord': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSldfEHwyUdGZarvTTLtGlfz8Cbv1rzz_KmSTspvcPqew&s=10', 
  'bristleback': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQg6X-Te6h4LbOWDJMMRf_MfAgfpN13eXK8BPVskxjMjg&s=10',
  'earthshaker': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQWnaeS7X0mDzMtWpbYVWJ-PrK2heU0eNNqA48ey2v4Zg&s=10', 
  'tusk': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAjSNBeT4ioOsLwRzNGk16r5ic3Au4LWv0qGsHVvqrpg&s=10', 
  'rubick': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRiCpR6iW97sx6WBR29JFSZOOJNhA8lFfHXfqwbe2XI-w&s=10', 
  'nyx-assassin': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQCzZTd5FNfmt5Gm1qeAfbfNnM3SnBbQ_WNfr9y7D4jVw&s=10', 
  'hoodwink': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwxgfZOVFOcfb6y4hFmYgKZpSDB8u_4-_E0GFJctb2gA&s=10', 
  'spirit-breaker': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNa2gyUkNNk4eAmUDBJG0AUtZfzz81w2AqtUFrxc57KQ&s=10',
  'crystal-maiden': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQILKhCaBka7RgfOrc8jh5DKOqFKQvSHdmk8UJAG3CC-g&s=10', 
  'lion': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQblD-dKWw2mJdtFWtFuPZmToxzEoQLE_5kA5ZnKgTS8A&s=10', 
  'dazzle': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqJkFOvztmtQJFkp0aH6fchBz98P-9CKwFkNLsBGTH6w&s=10', 
  'warlock': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3ElcqaT_tEhzsdZbQyRJdy-sJuZknxI6tO0DWSYKaHQ&s=10', 
  'witch-doctor': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbTw4odUomUZbLzu3DGrVrmAmD5-xpC__Y3KZHkpt40g&s=10', 
  'shadow-shaman': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-QO1jtuqEOQW5uz7hxfHv4m8jjyhmSDC4P6ip94vHwQ&s=10'
};
const imageUrl = hero => HERO_IMAGES[norm(hero.id)] || IMG_DIR + hero.id + IMG_EXT;

  const SLOT_POSITIONS = {
    radiant: { TOP: [3, 4], MID: [2], BOTTOM: [1, 5] },
    dire:    { TOP: [1, 5], MID: [2], BOTTOM: [3, 4] }
  };
  const DEMO = {
    radiant: { 1: 'juggernaut', 2: 'puck', 3: 'axe', 4: 'earthshaker', 5: 'crystal-maiden' },
    dire:    { 1: 'phantom-assassin', 2: 'invoker', 3: 'mars', 4: 'tusk', 5: 'lion' }
  };

  const slots = [];
  ['radiant', 'dire'].forEach(team => LANES.forEach(lane =>
    SLOT_POSITIONS[team][lane].forEach(position => slots.push({ team, lane, position, hero: null }))));
  let active = 0;
  let busy = false;

  const $ = id => document.getElementById(id);
  const analyzeBtn = $('analyze');
  const poolButtons = new Map();
  const slotViews = [];

  function el(tag, cls, text) {
    const n = document.createElement(tag);
    if (cls) n.className = cls;
    if (text !== undefined) n.textContent = text;
    return n;
  }

  function avatar(hero) {
    const wrap = el('span', 'avatar');
    const img = document.createElement('img');
    img.alt = '';
    img.addEventListener('error', () => {
      img.remove();
      wrap.textContent = hero.name.split(/[\s-]+/).map(w => w[0]).join('').slice(0, 2);
    });
    img.referrerPolicy = 'no-referrer';
    img.src = imageUrl(hero);
    wrap.appendChild(img);
    return wrap;
  }

  function buildPool() {
    for (let pos = 1; pos <= 5; pos++) {
      const row = el('div', 'pool-row');
      row.appendChild(el('h3', null, POSITION_TITLES[pos]));
      const grid = el('div', 'pool-grid');
      HEROES.filter(h => h.position === pos).forEach(h => {
        const b = el('button', 'hero');
        b.type = 'button';
        b.appendChild(avatar(h));
        b.appendChild(el('span', 'hero-name', h.name));
        b.addEventListener('click', () => pick(h));
        poolButtons.set(h.id, b);
        grid.appendChild(b);
      });
      row.appendChild(grid);
      $('pool').appendChild(row);
    }
  }

  function buildSlot(slot, i) {
    const wrap = el('div', 'slot');
    const main = el('button', 'slot-main');
    main.type = 'button';
    main.addEventListener('click', () => { active = i; update(); });
    const clear = el('button', 'slot-clear', '×');
    clear.type = 'button';
    clear.setAttribute('aria-label', 'Clear slot');
    clear.addEventListener('click', () => { slot.hero = null; active = i; update(); });
    wrap.append(main, clear);
    slotViews[i] = { wrap, main, clear };
    return wrap;
  }

  function buildTeams() {
    [['radiant', 'Radiant'], ['dire', 'Dire']].forEach(([team, title]) => {
      const card = el('div', 'team ' + team);
      card.appendChild(el('h3', null, title));
      LANES.forEach(lane => {
        const group = el('div', 'lane');
        group.appendChild(el('div', 'lane-title', LANE_TITLES[lane]));
        const row = el('div', 'lane-slots');
        slots.forEach((s, i) => { if (s.team === team && s.lane === lane) row.appendChild(buildSlot(s, i)); });
        group.appendChild(row);
        card.appendChild(group);
      });
      $('teams').appendChild(card);
    });
  }

  function nextEmpty(from) {
    for (let k = 1; k <= slots.length; k++) {
      const i = (from + k) % slots.length;
      if (!slots[i].hero) return i;
    }
    return -1;
  }

  function pick(hero) {
    if (active < 0) return;
    slots[active].hero = hero;
    active = nextEmpty(active);
    update();
  }

  function fillDemo() {
    slots.forEach(s => { s.hero = HERO_BY_ID.get(DEMO[s.team][s.position]) || null; });
    active = -1;
    update();
  }

  function reset() {
    slots.forEach(s => { s.hero = null; });
    active = 0;
    update();
  }

  function updateSlots() {
    slots.forEach((s, i) => {
      const v = slotViews[i];
      v.wrap.classList.toggle('active', i === active);
      v.wrap.classList.toggle('filled', !!s.hero);
      v.main.textContent = '';
      v.main.appendChild(s.hero ? avatar(s.hero) : el('span', 'avatar empty', '?'));
      v.main.appendChild(el('span', 'slot-pos', 'Pos ' + s.position));
      v.main.appendChild(el('span', 'slot-name', s.hero ? s.hero.name : 'Empty'));
      v.clear.hidden = !s.hero;
    });
  }

  function updatePool() {
    const slot = active >= 0 ? slots[active] : null;
    const taken = new Set(slots.filter(s => s.hero).map(s => s.hero.id));
    HEROES.forEach(h => {
      const b = poolButtons.get(h.id);
      const isPicked = taken.has(h.id);
      const wrongPos = !!slot && h.position !== slot.position;
      b.disabled = !slot || wrongPos || isPicked;
      b.classList.toggle('picked', isPicked);
      b.classList.toggle('dim', wrongPos);
    });
  }

  function updateControls() {
    const n = slots.filter(s => s.hero).length;
    analyzeBtn.disabled = n < slots.length || busy;
    $('status').textContent = n < slots.length ? 'Picked ' + n + '/' + slots.length + ' heroes' : 'Draft is complete';
  }

  function markStale() {
    if (!$('result').hidden) {
      $('result').classList.add('stale');
      $('stale-note').hidden = false;
    }
  }

  function update() {
    updateSlots();
    updatePool();
    updateControls();
    markStale();
  }

  function buildRequest() {
    const req = { radiant: [], dire: [] };
    slots.forEach(s => req[s.team].push({ heroId: s.hero.id, position: s.position, lane: s.lane }));
    return req;
  }

  async function analyze() {
    busy = true;
    updateControls();
    $('error').hidden = true;
    try {
      let res;
      try {
        res = await fetch(API_URL, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(buildRequest())
        });
      } catch (e) {
        throw new Error('Cannot reach the server');
      }
      let body = null;
      try { body = await res.json(); } catch (e) {}
      if (!res.ok) throw new Error((body && (body.error || body.message)) || 'Server error ' + res.status);
      renderResult(body);
    } catch (e) {
      $('error').textContent = e.message || 'Request failed';
      $('error').hidden = false;
    } finally {
      busy = false;
      updateControls();
    }
  }

  const signed = n => (n > 0 ? '+' : '') + n.toFixed(2);
  const sideText = d => Math.abs(d) < 0.005 ? 'Even' : (d > 0 ? 'Radiant' : 'Dire') + ' ahead by ' + Math.abs(d).toFixed(2);

  function diffBar(diff) {
    const bar = el('div', 'bar');
    const fill = el('div', 'fill ' + (diff > 0 ? 'radiant' : 'dire'));
    fill.style.width = Math.min(Math.abs(diff) / BAR_RANGE, 1) * 50 + '%';
    fill.style[diff > 0 ? 'left' : 'right'] = '50%';
    bar.appendChild(fill);
    return bar;
  }

  function laneCard(title, lane) {
    const card = el('div', 'card');
    card.appendChild(el('h4', null, title));
    card.appendChild(diffBar(lane.diff));
    card.appendChild(el('div', 'value', signed(lane.diff) + ' · ' + sideText(lane.diff)));
    const reasons = lane.reasons || (lane.explanation ? [lane.explanation] : []);
    if (reasons.length) {
      const ul = el('ul');
      reasons.forEach(r => ul.appendChild(el('li', null, r)));
      card.appendChild(ul);
    }
    return card;
  }

  function phaseCard(title, diff, label, warn) {
    const card = el('div', 'card');
    card.appendChild(el('h4', null, title));
    card.appendChild(diffBar(diff));
    card.appendChild(el('div', 'value', signed(diff) + ' · ' + label));
    if (warn) card.appendChild(el('span', 'badge', warn));
    return card;
  }

  function winConditions(d) {
    const items = [];
    const mid = d.midgame.diff, late = d.lategame.diff;
    if (Math.abs(mid) >= 0.5) items.push((mid > 0 ? 'Radiant' : 'Dire') + ': use the midgame tempo to apply pressure before the late game.');
    if (Math.abs(late) >= 0.5) items.push((late > 0 ? 'Radiant' : 'Dire') + ': stretch the game to the late stage, where the draft scales better.');
    if (!items.length) items.push('The drafts are close: execution decides the game.');
    return items;
  }

  function fillList(id, items) {
    const ul = $(id);
    ul.textContent = '';
    (items || []).forEach(t => ul.appendChild(el('li', null, t)));
  }

  function renderResult(d) {
    const lanes = $('lanes');
    lanes.textContent = '';
    lanes.appendChild(laneCard('Top', d.laning.top));
    lanes.appendChild(laneCard('Mid', d.laning.mid));
    lanes.appendChild(laneCard('Bottom', d.laning.bottom));

    const phases = $('phases');
    phases.textContent = '';
    if (typeof d.laning.overallDiff === 'number') {
      phases.appendChild(phaseCard('Overall laning', d.laning.overallDiff, sideText(d.laning.overallDiff)));
    } else {
      console.warn('overallDiff missing in /api/analyze response');
    }
    const x = typeof d.thresholdX === 'number' ? d.thresholdX : null;
    const warn = x !== null && Math.abs(d.midgame.diff) >= x ? 'Strong tempo lead (|diff| ≥ ' + x + ')' : null;
    phases.appendChild(phaseCard('Midgame', d.midgame.diff, d.midgame.label, warn));
    phases.appendChild(phaseCard('Late game', d.lategame.diff, d.lategame.label));

    fillList('summary', d.summary);
    fillList('conditions', winConditions(d));
    $('result').classList.remove('stale');
    $('stale-note').hidden = true;
    $('result').hidden = false;
  }

  async function loadHeroes() {
    const res = await fetch('http://127.0.0.1:8080/api/heroes');
    if (!res.ok) throw new Error('Heroes request failed: ' + res.status);
    return res.json();
  }

  buildTeams();
  analyzeBtn.addEventListener('click', analyze);
  $('demo').addEventListener('click', fillDemo);
  $('reset').addEventListener('click', reset);
  $('demo').disabled = true;
  update();

  loadHeroes().then(list => {
    HEROES = list;
    HERO_BY_ID = new Map(list.map(h => [norm(h.id), h]));
    buildPool();
    $('demo').disabled = false;
    update();
  }).catch(e => {
    $('error').textContent = 'Cannot load heroes: ' + e.message;
    $('error').hidden = false;
  });
})();