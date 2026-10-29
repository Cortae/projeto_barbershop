export default function Logo({ large }) {
  return (
    <img
      src="/logo.png"
      alt="JR Barbershop"
      className={`block h-auto object-contain ${large ? 'w-56' : 'w-32'}`}
    />
  )
}
