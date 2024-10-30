from cryptography.hazmat.primitives.asymmetric import dsa
from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey

# ruleid: cryptography-dsa
private_key = dsa.generate_private_key(
    key_size=1024,
)

params = dsa.generate_parameters(2048)

# ruleid: cryptography-dsa
private_key = cryptography.hazmat.primitives.asymmetric.ed25519.Ed25519PrivateKey.generate()

# ok: cryptography-dsa
private_key = Ed25519PrivateKey.generate()
