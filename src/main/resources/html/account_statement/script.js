function getDate() {
  const today = new Date();
  const options = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  };
  return today.toLocaleDateString('fr-CA', options);
}

function getTrimester() {
  const today = new Date();
  return `Session d'${today.getMonth() < 5 ? 'hiver' : 'automne'}`;
}

function getTime() {
  const now = new Date();
  return `${now.getHours()}:${now.getMinutes() < 10 ? 0 : ''}${now.getMinutes()}`;
}

function setCopies(copies) {
  const columns = ['title', 'edition', 'editor', 'added', 'sold', 'paid', 'price'];
  const tbody = document.getElementById('copies');

  copies.forEach((copy) => {
    const tr = document.createElement('tr');

    columns.forEach((column) => {
      const td = document.createElement('td');
      td.innerHTML = copy[column] || '';
      tr.appendChild(td);
    });

    tbody.appendChild(tr);
  });
}

const member = location.search.length ? JSON.parse(decodeURIComponent(location.search.replace('?data=', ''))) : {};
console.log(JSON.stringify(member));

document.getElementById('trimester').innerHTML = `${getTrimester()}, ${getDate()}, ${getTime()}`;
document.getElementById('date').innerHTML = getDate();

const names = document.querySelectorAll('.name');
for (let i = 0; i < names.length; i++) {
  names[i].innerHTML = member.name || '';
}

Object.keys(member).forEach((key) => {
  if (key === 'copies') {
    setCopies(member.copies);
  } else {
    const el = document.getElementById(key);

    if (el) {
      el.innerHTML = member[key];
    }
  }
});

window.print();
