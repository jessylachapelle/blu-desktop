function getXMLHttpRequest() {
	if (window.ActiveXObject) {
		try {
			return new ActiveXObject('Msxml2.XMLHTTP');
		} catch (e) {
			return new ActiveXObject('Microsoft.XMLHTTP');
		}
	}

	return new XMLHttpRequest();
}

const HTTP = {
	call: (method, url, data, callback) => {
		const xmlhttp = new getXMLHttpRequest();

		xmlhttp.onreadystatechange = () => {
			if (xmlhttp.readyState == xmlhttp.DONE) {
				if (xmlhttp.status === 200) {
					callback(null, JSON.parse(xmlhttp.responseText));
				} else {
					callback(xmlhttp.responseText);
				}
			}
		}

		xmlhttp.open(method, `${url}?req=${JSON.stringify(data)}`, true);
    xmlhttp.setRequestHeader('Content-type', 'application/json');
    xmlhttp.setRequestHeader('X-Authorization', '8ecf71749e3a5a5f02d585943e81849f');
		xmlhttp.send(data);
	}
};

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
		copy.transaction.forEach((transaction) => {
			switch (transaction.code) {
				case 'ADD':
					copy.added = transaction.date.split(' ')[0];
					break;
				case 'SELL':
				case 'SELL_PARENT':
					copy.sold = transaction.date.split(' ')[0];
					break;
				case 'PAY':
					copy.paid = transaction.date.split(' ')[0];
					break;
			}
		});

		copy.title = copy.item.name;
		copy.editor = copy.item.editor;
		copy.edition = copy.item.edition;
		copy.price += ' $';

    const tr = document.createElement('tr');

    columns.forEach((column) => {
      const td = document.createElement('td');
      td.innerHTML = copy[column] || '';
      tr.appendChild(td);
    });

    tbody.appendChild(tr);
  });
}

const data = {};
location.search.split('?')[1].split(/&/g).forEach((search) => {
	param = search.split('=');
	data[param[0]] = param[1];
});

const query = {
	data,
  function: 'select',
  object:'member',
};

HTTP.call('POST', 'http://aecs.info/blu/beta/api/index.php', query, (err, res) => {
	if (res) {
		const member = res.data;
		member.amount = data.amount || 0;
		member.address = `${member.address}, ${member.city.name} (${member.city.state.code}) ${member.zip}`

		const names = document.querySelectorAll('.name');
		for (let i = 0; i < names.length; i++) {
		  names[i].innerHTML = `${member.first_name} ${member.last_name}`;
		}

		Object.keys(member).forEach((key) => {
		  const el = document.getElementById(key);

	    if (el) {
	      el.innerHTML = member[key];
	    }
		});

		member.phone.forEach((phone, index) => {
			const el = document.getElementById(`phone${index + 1}`);

			if (el) {
				el.innerHTML = phone.number;
			}
		});

		setCopies(member.account.copies);
		document.getElementById('trimester').innerHTML = `${getTrimester()}, ${getDate()}, ${getTime()}`;
		document.getElementById('date').innerHTML = getDate();

		window.print();
	}
});
